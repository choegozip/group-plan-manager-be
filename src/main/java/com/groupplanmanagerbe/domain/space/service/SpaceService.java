package com.groupplanmanagerbe.domain.space.service;

import com.groupplanmanagerbe.domain.space.entity.Space;
import com.groupplanmanagerbe.domain.space.entity.SpaceMember;
import com.groupplanmanagerbe.domain.space.repository.SpaceMemberRepository;
import com.groupplanmanagerbe.domain.space.repository.SpaceRepository;
import com.groupplanmanagerbe.domain.user.entity.User;
import com.groupplanmanagerbe.domain.user.service.UserComponent;
import com.groupplanmanagerbe.global.common.enums.ApiErrorCode;
import com.groupplanmanagerbe.global.exception.custom.NotFoundException;
import com.groupplanmanagerbe.presentation.space.dto.request.CreateSpaceReq;
import com.groupplanmanagerbe.presentation.space.dto.request.UpdateSpaceReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SpaceService {

    private final SpaceRepository spaceRepository;
    private final SpaceMemberRepository spaceMemberRepository;
    private final UserComponent userComponent;

    @Transactional
    public void createSpace(CreateSpaceReq request, Long userId) {
        User user = userComponent.getById(userId);
        Space space = Space.of(request.name(), request.profileImageKey());

        SpaceMember spaceMember = SpaceMember.of(user, space);
        spaceMember.makeOwner();
        space.addMember(spaceMember);

        spaceRepository.save(space);
        spaceMemberRepository.save(spaceMember);
    }

    @Transactional
    public void updateSpace(Long spaceId, UpdateSpaceReq request, Long userId) {
        User user = userComponent.getById(userId);
        Space space = spaceRepository.findByIdAndUserId(spaceId, userId)
                .orElseThrow(() -> new NotFoundException(ApiErrorCode.SPACE_NOT_FOUND));

        space.updateSpaceInfo(request.name(), request.profileImageKey());
    }

    @Transactional
    public void deleteSpace(Long spaceId, Long userId) {
        User user = userComponent.getById(userId);
        Space space = spaceRepository.findByIdAndUserId(spaceId, userId)
                .orElseThrow(() -> new NotFoundException(ApiErrorCode.SPACE_NOT_FOUND));

        space.softDelete();
    }
}
