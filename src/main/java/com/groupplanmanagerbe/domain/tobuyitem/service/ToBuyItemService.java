package com.groupplanmanagerbe.domain.tobuyitem.service;

import com.groupplanmanagerbe.domain.space.entity.Space;
import com.groupplanmanagerbe.domain.space.service.SpaceComponent;
import com.groupplanmanagerbe.domain.tobuycomment.entity.ToBuyComment;
import com.groupplanmanagerbe.domain.tobuycomment.service.ToBuyCommentComponent;
import com.groupplanmanagerbe.domain.tobuyitem.entity.ToBuyItem;
import com.groupplanmanagerbe.domain.tobuyitem.entity.ToBuyManager;
import com.groupplanmanagerbe.domain.tobuyitem.event.ChangeToBuyMgrStatusEvent;
import com.groupplanmanagerbe.domain.tobuyitem.event.CreateToBuyEvent;
import com.groupplanmanagerbe.domain.tobuyitem.repository.ToBuyItemRepository;
import com.groupplanmanagerbe.domain.user.entity.User;
import com.groupplanmanagerbe.domain.user.service.UserComponent;
import com.groupplanmanagerbe.global.common.enums.ApiErrorCode;
import com.groupplanmanagerbe.global.common.response.page.CursorPageRequest;
import com.groupplanmanagerbe.global.exception.custom.InvalidException;
import com.groupplanmanagerbe.global.sse.SseService;
import com.groupplanmanagerbe.global.sse.dto.ManagerStatusData;
import com.groupplanmanagerbe.global.sse.dto.ToBuyData;
import com.groupplanmanagerbe.presentation.tobuyitem.dto.ToBuyListProjection;
import com.groupplanmanagerbe.presentation.tobuyitem.dto.request.CreateToBuyReq;
import com.groupplanmanagerbe.presentation.tobuyitem.dto.request.ParamReq;
import com.groupplanmanagerbe.presentation.tobuyitem.dto.request.UpdateManagerStatusReq;
import com.groupplanmanagerbe.presentation.tobuyitem.dto.request.UpdateToBuyReq;
import com.groupplanmanagerbe.presentation.tobuyitem.dto.response.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.groupingBy;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ToBuyItemService {

    private final ToBuyItemRepository toBuyItemRepository;
    private final ToBuyComponent toBuyComponent;
    private final SpaceComponent spaceComponent;
    private final UserComponent userComponent;
    private final ToBuyCommentComponent commentComponent;
    private final ApplicationEventPublisher eventPublisher;
    private final SseService sseService;

    @Transactional
    public ToBuyRes createToBuy(Long userId, CreateToBuyReq request, Long spaceId) {
        if (toBuyComponent.countBySpaceId(spaceId) > 10) {
           throw new InvalidException(ApiErrorCode.TO_BUY_LIMIT_EXCEEDED);
        }

        User user = userComponent.getByIdAndDeleteFalse(userId);
        Space space = spaceComponent.getByIdAndUserId(spaceId, userId, ApiErrorCode.SPACE_NOT_FOUND);
        ToBuyItem toBuy = createToBuyItem(request, user, space);
        List<ToBuyManager> managers = assignManagersToToBuy(request.managerIds(), space, toBuy);
        toBuy.setManagers(managers);
        toBuyItemRepository.save(toBuy);

        publishCreateEvent(user.getNickname(), request.title(), managers);

        return ToBuyRes.of(toBuy.getId());
    }

    @Transactional
    public ToBuyRes updateToBuy(Long userId, UpdateToBuyReq request, Long spaceId, Long toBuyId) {
        ToBuyItem toBuy = toBuyComponent.getByIdAndSpaceIdAndUserIdWithSpaceAndUser(toBuyId, spaceId, userId);
        List<ToBuyManager> managers = (request.managerIds() == null || request.managerIds().isEmpty())
                ? Collections.emptyList()
                : assignManagersToToBuy(request.managerIds(), toBuy.getSpace(), toBuy);
        updateToBuyItem(request, toBuy, managers);

        sseService.sendEvent(spaceId, sseService.TYPE_OF_TO_BUY, ToBuyData.of(toBuyId, request, managers));

        return ToBuyRes.of(toBuy.getId());
    }

    @Transactional
    public void deleteToBuy(Long userId, Long spaceId, Long toBuyId) {
        ToBuyItem toBuyItem = toBuyComponent.getByIdAndSpaceIdAndUserId(toBuyId, spaceId, userId);
        toBuyItemRepository.delete(toBuyItem);
    }

    @Transactional
    public UpdateManagerStatusRes updateManagerStatus(
            Long userId, UpdateManagerStatusReq request, Long spaceId, Long toBuyId, Long managerId
    ) {
        if (!userId.equals(managerId)) {
            throw new InvalidException(ApiErrorCode.MANAGER_NOT_FOUND);
        }

        ToBuyManager manager = toBuyComponent.getByIdAndSpaceIdAndToBuyIdWithToBuy(managerId, spaceId, toBuyId);
        manager.updateStatus(request.managerStatus());

        sseService.sendEvent(spaceId, sseService.TYPE_OF_TO_BUY, ManagerStatusData.of(toBuyId, manager));
        publishChangeStatusEvent(manager, request);

        return UpdateManagerStatusRes.of(manager.getStatus());
    }

    public ToBuyPageRes getToBuyList(Long userId, Long spaceId, CursorPageRequest request, ParamReq params) {
        List<ToBuyListProjection> toBuyList = toBuyComponent.getToBuyItemsNative(spaceId, userId, params, request);
        Map<Long, List<ToBuyManager>> managerMap = mapToBuyManagersByItemId(toBuyList, userId);
        List<ToBuyListRes> toBuyListResList = toRes(toBuyList, managerMap);
        return ToBuyPageRes.of(toBuyListResList, request.size());
    }

    public ToBuyDetailRes getToBuy(Long userId, Long spaceId, Long toBuyId) {
        ToBuyItem toBuy = toBuyComponent.getByIdAndSpaceId(toBuyId, spaceId);
        List<ToBuyComment> comments = commentComponent.getCommentList(toBuyId);
        List<ToBuyManager> managers = toBuyComponent.getAllByToBuyItemId(toBuyId, userId);
        return ToBuyDetailRes.of(toBuy, comments, managers);
    }

    // === Private Methods ===
    private void publishCreateEvent(String author, String item, List<ToBuyManager> managers) {
        eventPublisher.publishEvent(new CreateToBuyEvent(author, item, managers));
    }

    private void publishChangeStatusEvent(ToBuyManager manager, UpdateManagerStatusReq request) {
        ToBuyItem toBuy = manager.getToBuyItem();
        eventPublisher.publishEvent(new ChangeToBuyMgrStatusEvent(
                toBuy.getUser().getId(), manager.getUser().getNickname(), toBuy.getTitle(), request.managerStatus()));
    }

    private ToBuyItem createToBuyItem(CreateToBuyReq request, User user, Space space) {
        return ToBuyItem.of(space, user, request.title(), request.quantity(), request.dueDate(), request.urgency(),
                request.imageUrl(), request.referenceUrl(), request.memo());
    }

    private List<ToBuyManager> assignManagersToToBuy(List<Long> memberIds, Space space, ToBuyItem toBuyId) {
        Set<Long> setMemberIds = Set.copyOf(memberIds);
        return space.getMembers().stream()
                .filter(member -> setMemberIds.contains(member.getUser().getId()))
                .map(member -> ToBuyManager.of(member.getUser(), toBuyId))
                .toList();
    }

    private void updateToBuyItem(UpdateToBuyReq request, ToBuyItem toBuy, List<ToBuyManager> managers) {
        toBuy.updateToBuyItem(
                request.title(), request.quantity(), request.dueDate(), request.urgency(),
                request.imageUrl(), request.referenceUrl(), request.memo(), managers);
    }

    private Map<Long, List<ToBuyManager>> mapToBuyManagersByItemId(List<ToBuyListProjection> toBuyList, Long userId) {
        List<Long> toBuyIds = toBuyList.stream().map(ToBuyListProjection::getToBuyId).toList();
        List<ToBuyManager> allManagers = toBuyComponent.getByToBuyItemIdsWithUser(toBuyIds, userId);
        return allManagers.stream()
                .collect(groupingBy(m -> m.getToBuyItem().getId()));
    }

    private List<ToBuyListRes> toRes(List<ToBuyListProjection> toBuyList, Map<Long, List<ToBuyManager>> managerMap) {
        return toBuyList.stream()
                .map(item -> ToBuyListRes.of(item, managerMap.getOrDefault(item.getToBuyId(), List.of())))
                .toList();
    }
}
