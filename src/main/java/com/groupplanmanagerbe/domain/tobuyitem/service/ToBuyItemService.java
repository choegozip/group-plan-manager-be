package com.groupplanmanagerbe.domain.tobuyitem.service;

import com.groupplanmanagerbe.domain.space.entity.Space;
import com.groupplanmanagerbe.domain.space.entity.SpaceMember;
import com.groupplanmanagerbe.domain.space.service.SpaceComponent;
import com.groupplanmanagerbe.domain.tobuyitem.entity.ToBuyItem;
import com.groupplanmanagerbe.domain.tobuyitem.entity.ToBuyManager;
import com.groupplanmanagerbe.domain.tobuyitem.repository.ToBuyItemRepository;
import com.groupplanmanagerbe.domain.tobuyitem.repository.ToBuyManagerRepository;
import com.groupplanmanagerbe.domain.user.entity.User;
import com.groupplanmanagerbe.domain.user.service.UserComponent;
import com.groupplanmanagerbe.global.common.enums.ApiErrorCode;
import com.groupplanmanagerbe.global.exception.custom.InvalidException;
import com.groupplanmanagerbe.global.exception.custom.NotFoundException;
import com.groupplanmanagerbe.presentation.tobuyitem.dto.request.CreateToBuyReq;
import com.groupplanmanagerbe.presentation.tobuyitem.dto.request.UpdateManagerStatusReq;
import com.groupplanmanagerbe.presentation.tobuyitem.dto.request.UpdateToBuyReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ToBuyItemService {

    private final ToBuyItemRepository toBuyItemRepository;
    private final ToBuyManagerRepository toBuyManagerRepository;
    private final SpaceComponent spaceComponent;
    private final UserComponent userComponent;

    @Transactional
    public void createToBuy(Long userId, CreateToBuyReq request, Long spaceId) {
        User user = userComponent.getByIdAndDeleteFalse(userId);
        Space space = spaceComponent.getByIdAndUserId(spaceId, userId, ApiErrorCode.SPACE_NOT_FOUND);

        ToBuyItem toBuyItem = ToBuyItem.of(
                space, user, request.title(), request.quantity(), request.dueDate(), request.urgency(),
                request.imageUrl(), request.referenceUrl(), request.memo());

        List<ToBuyManager> managers = createToBuy(request.managerIds(), space, toBuyItem);
        toBuyItem.setManagers(managers);

        toBuyItemRepository.save(toBuyItem);
    }

    @Transactional
    public void updateToBuy(Long userId, UpdateToBuyReq request, Long spaceId, Long toBuyItemId) {
        ToBuyItem toBuyItem = toBuyItemRepository.findByIdAndUserIdWithSpace(toBuyItemId, userId)
                .orElseThrow(() -> new NotFoundException(ApiErrorCode.TO_BUY_NOT_FOUND));
        validateSpaceId(toBuyItem, spaceId);

        List<ToBuyManager> managers = Collections.emptyList();
        if (request.managerIds() != null && !request.managerIds().isEmpty()) {
            managers = createToBuy(request.managerIds(), toBuyItem.getSpace(), toBuyItem);
        }

        toBuyItem.updateToBuyItem(
                request.title(), request.quantity(), request.dueDate(), request.urgency(),
                request.imageUrl(), request.referenceUrl(), request.memo(), managers);
    }

    @Transactional
    public void deleteToBuy(Long userId, Long spaceId, Long toBuyItemId) {
        ToBuyItem toBuyItem = toBuyItemRepository.findByIdAndUserId(toBuyItemId, userId)
                .orElseThrow(() -> new NotFoundException(ApiErrorCode.TO_BUY_NOT_FOUND));

        validateSpaceId(toBuyItem, spaceId);

        toBuyItemRepository.delete(toBuyItem);
    }

    @Transactional
    public String updateManagerStatus(
            Long userId, UpdateManagerStatusReq request, Long spaceId, Long toBuyItemId, Long managerId
    ) {
        ToBuyManager manager = toBuyManagerRepository.findByIdAndUserIdWithToBuyAndSpace(managerId, userId)
                .orElseThrow(() -> new NotFoundException(ApiErrorCode.MANAGER_NOT_FOUND));
        validateToBuyManager(manager, spaceId, toBuyItemId);
        manager.updateStatus(request.managerStatus());
        return request.managerStatus();
    }

    private List<ToBuyManager> createToBuy(List<Long> memberIds, Space space, ToBuyItem toBuyItem) {
        Set<Long> setMemberIds = Set.copyOf(memberIds);
        return space.getMembers().stream()
                .filter(member -> setMemberIds.contains(member.getUser().getId()))
                .map(member -> ToBuyManager.of(member.getUser(), toBuyItem))
                .toList();
    }

    private void validateSpaceId(ToBuyItem item, Long spaceId) {
        if (!item.getSpace().getId().equals(spaceId)) {
            throw new InvalidException(ApiErrorCode.INVALID_SPACE_ID);
        }
    }

    private void validateToBuyManager(ToBuyManager manager, Long spaceId, Long toBuyItemId) {
        if (!manager.getToBuyItem().getSpace().getId().equals(spaceId)) {
            throw new InvalidException(ApiErrorCode.INVALID_SPACE_ID);
        }

        if (!manager.getToBuyItem().getId().equals(toBuyItemId)) {
            throw new InvalidException(ApiErrorCode.INVALID_TO_BUY_ID);
        }
    }
}
