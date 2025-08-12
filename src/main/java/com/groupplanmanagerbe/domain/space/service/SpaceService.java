package com.groupplanmanagerbe.domain.space.service;

import com.groupplanmanagerbe.domain.space.entity.Space;
import com.groupplanmanagerbe.domain.space.entity.SpaceMember;
import com.groupplanmanagerbe.domain.space.repository.SpaceRepository;
import com.groupplanmanagerbe.domain.user.entity.User;
import com.groupplanmanagerbe.domain.user.service.UserComponent;
import com.groupplanmanagerbe.global.common.enums.ApiErrorCode;
import com.groupplanmanagerbe.global.exception.custom.InvalidException;
import com.groupplanmanagerbe.presentation.space.dto.request.CreateSpaceReq;
import com.groupplanmanagerbe.presentation.space.dto.request.UpdateSpaceReq;
import com.groupplanmanagerbe.presentation.space.dto.response.space.SpaceCreateRes;
import com.groupplanmanagerbe.presentation.space.dto.response.space.SpaceRes;
import com.groupplanmanagerbe.presentation.space.dto.response.space.SpacesListRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SpaceService {

    private final SpaceRepository spaceRepository;
    private final SpaceComponent spaceComponent;
    private final UserComponent userComponent;

    @Transactional
    public SpaceCreateRes createSpace(CreateSpaceReq request, Long userId) {
        User user = userComponent.getByIdAndDeleteFalse(userId);

        if (spaceComponent.countSpacesBelongingToUser(userId) >= 10) {
            throw new InvalidException(ApiErrorCode.SPACE_LIMIT_EXCEEDED);
        }
        Space space = Space.of(request.name());
        SpaceMember spaceMember = SpaceMember.of(user, space);
        spaceMember.makeOwner();
        spaceRepository.save(space);

        return SpaceCreateRes.of(space.getId());
    }

    @Transactional
    public void updateSpace(Long spaceId, UpdateSpaceReq request, Long userId) {
        Space space = spaceComponent.getByIdAndUserIdWithMember(spaceId, userId, ApiErrorCode.SPACE_NOT_FOUND);
        checkIsOwner(space, userId);
        space.updateSpaceInfo(request.name());
    }

    @Transactional
    public void deleteSpace(Long spaceId, Long userId) {
        Space space = spaceComponent.getByIdAndUserIdWithMember(spaceId, userId, ApiErrorCode.SPACE_NOT_FOUND);
        checkIsOwner(space, userId);
        space.softDelete();
    }

    public SpacesListRes getSpaces(Long userId) {
        List<Space> spaces = spaceRepository.findAllByUserId(userId);
        return SpacesListRes.of(spaces, userId);
    }

    public SpaceRes getSpace(Long userId, Long spaceId) {
        Space space = spaceComponent.getByIdAndUserIdWithMember(spaceId, userId, ApiErrorCode.SPACE_NOT_FOUND);
        return SpaceRes.from(space);
    }

    private void checkIsOwner(Space space, Long userId) {
        SpaceMember member = space.getMember(userId);
        if (!member.isOwner()) {
            throw new InvalidException(ApiErrorCode.PERMISSION_DENIED);
        }
    }
}
