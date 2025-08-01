package com.groupplanmanagerbe.domain.tobuyitem.service;

import com.groupplanmanagerbe.domain.space.entity.Space;
import com.groupplanmanagerbe.domain.space.entity.SpaceMember;
import com.groupplanmanagerbe.domain.space.service.SpaceComponent;
import com.groupplanmanagerbe.domain.tobuyitem.entity.ToBuyItem;
import com.groupplanmanagerbe.domain.tobuyitem.entity.ToBuyManager;
import com.groupplanmanagerbe.domain.tobuyitem.repository.ToBuyItemRepository;
import com.groupplanmanagerbe.domain.user.entity.User;
import com.groupplanmanagerbe.domain.user.service.UserComponent;
import com.groupplanmanagerbe.global.common.enums.ApiErrorCode;
import com.groupplanmanagerbe.global.exception.custom.InvalidException;
import com.groupplanmanagerbe.global.exception.custom.NotFoundException;
import com.groupplanmanagerbe.presentation.tobuyitem.dto.request.CreateToBuyReq;
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
    private final SpaceComponent spaceComponent;
    private final UserComponent userComponent;

    @Transactional
    public void createToBuy(Long userId, CreateToBuyReq request, Long spaceId) {
        User user = userComponent.getByIdAndDeleteFalse(userId);
        Space space = spaceComponent.getByIdAndUserId(spaceId, userId, ApiErrorCode.SPACE_NOT_FOUND);
        Set<Long> memberIds = Set.copyOf(request.managerIds());
        List<SpaceMember> members = space.getMembers().stream()
                .filter(member -> memberIds.contains(member.getUser().getId()))
                .toList();

        ToBuyItem toBuyItem = ToBuyItem.of(
                space,
                user,
                request.title(),
                request.quantity(),
                request.dueDate(),
                request.urgency(),
                request.imageUrl(),
                request.referenceUrl(),
                request.memo());

        List<ToBuyManager> managers = members.stream()
                .map(member -> ToBuyManager.of(member.getUser(), toBuyItem))
                .toList();
        toBuyItem.setManagers(managers);

        toBuyItemRepository.save(toBuyItem);
    }

    @Transactional
    public void updateToBuy(Long userId, UpdateToBuyReq request, Long spaceId, Long toBuyItemId) {
        ToBuyItem toBuyItem = toBuyItemRepository.findByIdAndUserIdWithSpace(toBuyItemId, userId)
                .orElseThrow(() -> new NotFoundException(ApiErrorCode.TO_BUY_NOT_FOUND));
        if (!toBuyItem.getSpace().getId().equals(spaceId)) {
            throw new InvalidException(ApiErrorCode.INVALID_SPACE_ID);
        }

        List<ToBuyManager> managers = Collections.emptyList();
        if (request.managerIds() != null && !request.managerIds().isEmpty()) {
            Set<Long> memberIds = Set.copyOf(request.managerIds());
            List<SpaceMember> members = toBuyItem.getSpace().getMembers().stream()
                    .filter(member -> memberIds.contains(member.getUser().getId()))
                    .toList();
            managers = members.stream()
                    .map(member -> ToBuyManager.of(member.getUser(), toBuyItem))
                    .toList();
        }

        toBuyItem.updateToBuyItem(
                request.title(), request.quantity(), request.dueDate(), request.urgency(),
                request.imageUrl(), request.referenceUrl(), request.memo(), managers);
    }
}
