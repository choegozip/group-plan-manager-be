package com.groupplanmanagerbe.domain.space.service;

import com.groupplanmanagerbe.domain.space.entity.Space;
import com.groupplanmanagerbe.domain.space.entity.SpaceInvited;
import com.groupplanmanagerbe.domain.space.repository.SpaceInvitedRepository;
import com.groupplanmanagerbe.domain.space.repository.SpaceRepository;
import com.groupplanmanagerbe.domain.user.service.UserComponent;
import com.groupplanmanagerbe.global.common.enums.ApiErrorCode;
import com.groupplanmanagerbe.global.exception.custom.NotFoundException;
import com.groupplanmanagerbe.presentation.space.dto.response.InviteSpaceMemberRes;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SpaceMemberService {

    private final SpaceInvitedRepository spaceInvitedRepository;
    private final SpaceRepository spaceRepository;
    private final UserComponent userComponent;

    @Value("${app.base-url}")
    private String baseUrl;

    @Value("${app.invitation-path}")
    private String invitationPath;

    @Transactional
    public InviteSpaceMemberRes inviteMember(Long userId, Long spaceId) {
        Space space = spaceRepository.findByIdAndUserId(spaceId, userId)
                .orElseThrow(() -> new NotFoundException(ApiErrorCode.SPACE_NOT_FOUND));

        SpaceInvited invited = spaceInvitedRepository.findBySpaceId(spaceId)
                .orElseGet(() -> {
                    SpaceInvited newInvited = SpaceInvited.of(space);
                    return spaceInvitedRepository.save(newInvited);
                });

        String invitationUrl = generateInvitationUrl(invited.getInvitedKey());
        return InviteSpaceMemberRes.of(invitationUrl);
    }

    private String generateInvitationUrl(String inviteKey) {
        return baseUrl + "/" + invitationPath + "/" + inviteKey;
    }
}
