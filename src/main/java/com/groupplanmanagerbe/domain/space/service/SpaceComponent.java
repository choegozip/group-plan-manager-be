package com.groupplanmanagerbe.domain.space.service;

import com.groupplanmanagerbe.domain.space.entity.Space;
import com.groupplanmanagerbe.domain.space.entity.SpaceMember;
import com.groupplanmanagerbe.domain.space.repository.SpaceMemberRepository;
import com.groupplanmanagerbe.domain.space.repository.SpaceRepository;
import com.groupplanmanagerbe.global.common.enums.ApiErrorCode;
import com.groupplanmanagerbe.global.exception.custom.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

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

    public void isExistById(Long spaceId, Long userId) {
        boolean isExist = spaceRepository.existsByIdAndUserId(spaceId, userId);
        if (!isExist) {
            throw new NotFoundException(ApiErrorCode.SPACE_NOT_FOUND);
        }
    }

    public Space getRefBySpaceId(Long spaceId) {
        return spaceRepository.getReferenceById(spaceId);
    }

    public List<SpaceMember> getSpaceMembers(List<Long> memberId, Long spaceId) {
        return spaceMemberRepository.findAllByIdAndSpaceId(memberId, spaceId);
    }
}
