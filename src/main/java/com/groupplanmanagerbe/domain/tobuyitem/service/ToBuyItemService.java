package com.groupplanmanagerbe.domain.tobuyitem.service;

import com.groupplanmanagerbe.domain.space.entity.Space;
import com.groupplanmanagerbe.domain.space.entity.SpaceMember;
import com.groupplanmanagerbe.domain.space.service.SpaceComponent;
import com.groupplanmanagerbe.domain.tobuycomment.entity.ToBuyComment;
import com.groupplanmanagerbe.domain.tobuycomment.service.ToBuyCommentComponent;
import com.groupplanmanagerbe.domain.tobuyitem.entity.ToBuyItem;
import com.groupplanmanagerbe.domain.tobuyitem.entity.ToBuyManager;
import com.groupplanmanagerbe.domain.tobuyitem.repository.ToBuyItemRepository;
import com.groupplanmanagerbe.domain.tobuyitem.repository.ToBuyManagerRepository;
import com.groupplanmanagerbe.domain.user.entity.User;
import com.groupplanmanagerbe.domain.user.service.UserComponent;
import com.groupplanmanagerbe.global.common.enums.ApiErrorCode;
import com.groupplanmanagerbe.global.common.response.page.CursorPageRequest;
import com.groupplanmanagerbe.global.exception.custom.InvalidException;
import com.groupplanmanagerbe.global.exception.custom.NotFoundException;
import com.groupplanmanagerbe.presentation.tobuyitem.dto.ToBuyListProjection;
import com.groupplanmanagerbe.presentation.tobuyitem.dto.request.CreateToBuyReq;
import com.groupplanmanagerbe.presentation.tobuyitem.dto.request.UpdateManagerStatusReq;
import com.groupplanmanagerbe.presentation.tobuyitem.dto.request.UpdateToBuyReq;
import com.groupplanmanagerbe.presentation.tobuyitem.dto.response.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final ToBuyManagerRepository toBuyManagerRepository;
    private final SpaceComponent spaceComponent;
    private final UserComponent userComponent;
    private final ToBuyCommentComponent commentComponent;

    @Transactional
    public ToBuyRes createToBuy(Long userId, CreateToBuyReq request, Long spaceId) {
        User user = userComponent.getByIdAndDeleteFalse(userId);
        Space space = spaceComponent.getByIdAndUserId(spaceId, userId, ApiErrorCode.SPACE_NOT_FOUND);

        ToBuyItem toBuy = ToBuyItem.of(
                space, user, request.title(), request.quantity(), request.dueDate(), request.urgency(),
                request.imageUrl(), request.referenceUrl(), request.memo());
        List<ToBuyManager> managers = createToBuy(request.managerIds(), space, toBuy);
        toBuy.setManagers(managers);

        toBuyItemRepository.save(toBuy);

        return ToBuyRes.of(toBuy.getId());
    }

    @Transactional
    public ToBuyRes updateToBuy(Long userId, UpdateToBuyReq request, Long spaceId, Long toBuyId) {
        ToBuyItem toBuy = toBuyItemRepository.findByIdAndUserIdWithSpaceAndUser(toBuyId, userId)
                .orElseThrow(() -> new NotFoundException(ApiErrorCode.TO_BUY_NOT_FOUND));
        validateSpaceId(toBuy, spaceId);

        List<ToBuyManager> managers = Collections.emptyList();
        if (request.managerIds() != null && !request.managerIds().isEmpty()) {
            managers = createToBuy(request.managerIds(), toBuy.getSpace(), toBuy);
        }

        toBuy.updateToBuyItem(
                request.title(), request.quantity(), request.dueDate(), request.urgency(),
                request.imageUrl(), request.referenceUrl(), request.memo(), managers);

        return ToBuyRes.of(toBuy.getId());
    }

    @Transactional
    public void deleteToBuy(Long userId, Long spaceId, Long toBuyId) {
        ToBuyItem toBuyItem = toBuyItemRepository.findByIdAndUserId(toBuyId, userId)
                .orElseThrow(() -> new NotFoundException(ApiErrorCode.TO_BUY_NOT_FOUND));
        validateSpaceId(toBuyItem, spaceId);

        toBuyItemRepository.delete(toBuyItem);
    }

    @Transactional
    public UpdateManagerStatusRes updateManagerStatus(
            Long userId, UpdateManagerStatusReq request, Long spaceId, Long toBuyId, Long managerId
    ) {
        validateMatchUserId(userId, managerId);
        ToBuyManager manager = toBuyManagerRepository.findByIddWithToBuyAndSpace(managerId)
                .orElseThrow(() -> new NotFoundException(ApiErrorCode.MANAGER_NOT_FOUND));
        validateToBuyManager(manager, spaceId, toBuyId);
        manager.updateStatus(request.managerStatus());
        return UpdateManagerStatusRes.of(manager.getStatus());
    }

    public ToBuyPageRes getToBuyList(Long userId, Long spaceId, CursorPageRequest request) {
        List<ToBuyListProjection> toBuy = toBuyItemRepository.findToBuyItemsNative(
                spaceId, userId, request.managerId(), request.urgency(), request.cursor(),
                request.direction(), request.size());

        Map<Long, List<ToBuyManager>> managerMap = mapToBuyManagersByItemId(toBuy);
        List<ToBuyListRes> toBuyListResList = toBuy.stream()
                .map(item -> ToBuyListRes.of(item, managerMap.getOrDefault(item.getToBuyId(), List.of())))
                .toList();

        return ToBuyPageRes.of(toBuyListResList, request.size());
    }

    public ToBuyDetailRes getToBuy(Long userId, Long spaceId, Long toBuyId) {
        ToBuyItem toBuy = toBuyItemRepository.findByIdWithSpaceAndUser(toBuyId)
                .orElseThrow(() -> new NotFoundException(ApiErrorCode.TO_BUY_NOT_FOUND));
        validateSpaceId(toBuy, spaceId);
        validateMemberId(toBuy, userId);

        List<ToBuyComment> comments = commentComponent.getCommentList(toBuyId);
        List<ToBuyManager> managers = toBuyManagerRepository.findAllByToBuyItemId(toBuyId);
        return ToBuyDetailRes.of(toBuy, comments, managers);
    }

    // === Private Methods ===
    private List<ToBuyManager> createToBuy(List<Long> memberIds, Space space, ToBuyItem toBuyId) {
        Set<Long> setMemberIds = Set.copyOf(memberIds);
        return space.getMembers().stream()
                .filter(member -> setMemberIds.contains(member.getUser().getId()))
                .map(member -> ToBuyManager.of(member.getUser(), toBuyId))
                .toList();
    }

    private Map<Long, List<ToBuyManager>> mapToBuyManagersByItemId(List<ToBuyListProjection> toBuyList) {
        List<Long> toBuyIds = toBuyList.stream().map(ToBuyListProjection::getToBuyId).toList();
        List<ToBuyManager> allManagers = toBuyManagerRepository.findByToBuyItemIdsWithUser(toBuyIds);
        return allManagers.stream()
                .collect(groupingBy(m -> m.getToBuyItem().getId()));
    }

    private void validateSpaceId(ToBuyItem item, Long spaceId) {
        if (!item.getSpace().getId().equals(spaceId)) {
            throw new InvalidException(ApiErrorCode.INVALID_SPACE_ID);
        }
    }

    private void validateToBuyManager(ToBuyManager manager, Long spaceId, Long toBuyId) {
        if (!manager.getToBuyItem().getSpace().getId().equals(spaceId)) {
            throw new InvalidException(ApiErrorCode.INVALID_SPACE_ID);
        }

        if (!manager.getToBuyItem().getId().equals(toBuyId)) {
            throw new InvalidException(ApiErrorCode.INVALID_TO_BUY_ID);
        }
    }

    private void validateMemberId(ToBuyItem toBuy, Long userId) {
        boolean isMember = toBuy.getSpace().getMembers().stream()
                .anyMatch(member -> member.getUser().getId().equals(userId));
        if (!isMember) {
            throw new InvalidException(ApiErrorCode.TO_BUY_NOT_FOUND);
        }
    }

    private void validateMatchUserId(Long userId, Long managerId) {
        if (!userId.equals(managerId)) {
            throw new InvalidException(ApiErrorCode.MANAGER_NOT_FOUND);
        }
    }
}
