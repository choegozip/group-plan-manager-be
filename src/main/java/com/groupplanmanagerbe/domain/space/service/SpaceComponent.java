package com.groupplanmanagerbe.domain.space.service;

import com.groupplanmanagerbe.domain.space.entity.Space;
import com.groupplanmanagerbe.domain.space.repository.SpaceMemberRepository;
import com.groupplanmanagerbe.domain.space.repository.SpaceRepository;
import com.groupplanmanagerbe.global.common.enums.ApiErrorCode;
import com.groupplanmanagerbe.global.exception.custom.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class SpaceComponent {

    private final SpaceRepository spaceRepository;
    private final SpaceMemberRepository spaceMemberRepository;

    public Space getByIdAndUserId(Long spaceId, Long userId, ApiErrorCode errorCode) {
        return spaceRepository.findByIdAndUserId(spaceId, userId)
                .orElseThrow(() -> new NotFoundException(errorCode));
    }

    public Space getByIdAndOwnerId(Long ownerId, Long userId, ApiErrorCode errorCode) {
        return spaceRepository.findByIdAndOwnerUserId(ownerId, userId)
                .orElseThrow(() ->  new NotFoundException(errorCode));
    }

    public int countSpacesBelongingToUser(Long userId) {
        return spaceMemberRepository.countByUserId(userId);
    }
}
