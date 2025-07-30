package com.groupplanmanagerbe.domain.space.service;

import com.groupplanmanagerbe.domain.space.entity.Space;
import com.groupplanmanagerbe.domain.space.entity.SpaceInvited;
import com.groupplanmanagerbe.domain.space.entity.SpaceMember;
import com.groupplanmanagerbe.domain.space.repository.SpaceInvitedRepository;
import com.groupplanmanagerbe.domain.space.repository.SpaceMemberRepository;
import com.groupplanmanagerbe.domain.space.repository.SpaceRepository;
import com.groupplanmanagerbe.domain.user.entity.User;
import com.groupplanmanagerbe.domain.user.service.UserComponent;
import com.groupplanmanagerbe.global.common.enums.ApiErrorCode;
import com.groupplanmanagerbe.global.exception.custom.ConflictException;
import com.groupplanmanagerbe.global.exception.custom.InvalidException;
import com.groupplanmanagerbe.global.exception.custom.NotFoundException;
import com.groupplanmanagerbe.presentation.space.dto.request.JoinSpaceReq;
import com.groupplanmanagerbe.presentation.space.dto.response.spacemember.InviteSpaceMemberRes;
import com.groupplanmanagerbe.presentation.space.dto.response.spacemember.JoinSpaceRes;
import com.groupplanmanagerbe.presentation.space.dto.response.spacemember.SpaceMembersRes;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SpaceMemberService {

    private final SpaceInvitedRepository spaceInvitedRepository;
    private final SpaceRepository spaceRepository;
    private final SpaceMemberRepository spaceMemberRepository;
    private final UserComponent userComponent;

    @Value("${app.base-url}")
    private String baseUrl;

    @Value("${app.invitation-path}")
    private String invitationPath;

    public static final String SPACE_BASE_PATH = "spaces";


    @Transactional
    public InviteSpaceMemberRes inviteMember(Long userId, Long spaceId) {
        Space space = spaceRepository.findByIdAndUserId(spaceId, userId)
                .orElseThrow(() -> new NotFoundException(ApiErrorCode.SPACE_NOT_FOUND));

        SpaceInvited invited = spaceInvitedRepository.findBySpaceId(spaceId)
                .orElseGet(() -> {
                    SpaceInvited newInvited = SpaceInvited.of(space);
                    return spaceInvitedRepository.save(newInvited);
                });

        String invitationUrl = generateInvitationUrl(invited.getInviteKey());
        return InviteSpaceMemberRes.of(invitationUrl);
    }

    @Transactional
    public JoinSpaceRes joinSpace(Long userId, JoinSpaceReq request) {
        SpaceInvited invited = spaceInvitedRepository.findByInviteKeyAndDeleted(request.inviteKey())
                .orElseThrow(() -> new NotFoundException(ApiErrorCode.INVITATION_NOT_FOUND));
        Space space = invited.getSpace();
        User invitedUser = userComponent.getByIdAndDeleteFalse(userId);

        boolean alreadyJoined = space.getMembers().stream()
                .anyMatch(member -> member.getUser().equals(invitedUser));
        if (alreadyJoined) {
            throw new ConflictException(ApiErrorCode.SPACE_MEMBER_ALREADY_JOINED);
        }
        SpaceMember.of(invitedUser, space);

        spaceInvitedRepository.delete(invited);

        String landingUrl = generateLandingUrl(space.getId());
        return JoinSpaceRes.of(space, landingUrl);
    }

    @Transactional
    public void deleteMember(Long ownerId, Long spaceId, Long targetMemberUserId) {
        Space space = spaceRepository.findByIdAndOwnerUserId(spaceId, ownerId)
                .orElseThrow(() -> new NotFoundException(ApiErrorCode.SPACE_DELETE_FORBIDDEN));
        if (ownerId.equals(targetMemberUserId)) {
            throw new InvalidException(ApiErrorCode.OWNER_CANNOT_QUIT_SPACE);
        }
        space.deleteMember(targetMemberUserId);
    }

    public List<SpaceMembersRes> getSpaceMember(Long userId, Long spaceId) {
        Space space = spaceRepository.findByIdAndUserId(spaceId, userId)
                .orElseThrow(() -> new NotFoundException(ApiErrorCode.SPACE_MEMBER_GET_FORBIDDEN));
        List<SpaceMember> members = space.getMembers();

        return members.stream()
                .map(SpaceMembersRes::from)
                .collect(Collectors.toList());
    }

    private String generateLandingUrl(Long spaceId) {
        return baseUrl + "/" + SPACE_BASE_PATH + "/" + spaceId;
    }

    private String generateInvitationUrl(String inviteKey) {
        return baseUrl + "/" + invitationPath + "/" + inviteKey;
    }
}
