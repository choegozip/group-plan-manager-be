package com.groupplanmanagerbe.domain.space.service;

import com.groupplanmanagerbe.domain.space.entity.Space;
import com.groupplanmanagerbe.domain.space.entity.SpaceInvited;
import com.groupplanmanagerbe.domain.space.entity.SpaceMember;
import com.groupplanmanagerbe.domain.space.repository.SpaceInvitedRepository;
import com.groupplanmanagerbe.domain.user.entity.User;
import com.groupplanmanagerbe.domain.user.service.UserComponent;
import com.groupplanmanagerbe.global.common.enums.ApiErrorCode;
import com.groupplanmanagerbe.global.exception.custom.ConflictException;
import com.groupplanmanagerbe.global.exception.custom.InvalidException;
import com.groupplanmanagerbe.global.exception.custom.NotFoundException;
import com.groupplanmanagerbe.presentation.space.dto.request.JoinSpaceReq;
import com.groupplanmanagerbe.presentation.space.dto.response.spacemember.InviteSpaceMemberRes;
import com.groupplanmanagerbe.presentation.space.dto.response.spacemember.InvitedSpaceRes;
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
    private final SpaceComponent spaceComponent;
    private final UserComponent userComponent;

    @Value("${app.base-url}")
    private String baseUrl;

    @Value("${app.invitation-path}")
    private String invitationPath;

    public static final String SPACE_BASE_PATH = "spaces";
    public static final int MAX_MEMBERS = 10;

    @Transactional
    public InviteSpaceMemberRes inviteMember(Long userId, Long spaceId) {
        Space space = spaceComponent.getByIdAndUserId(spaceId, userId, ApiErrorCode.SPACE_NOT_FOUND);
        SpaceInvited invited = findOrCreateInvited(space, spaceId);

        String invitationUrl = generateInvitationUrl(invited.getInviteKey());
        return InviteSpaceMemberRes.of(invitationUrl);
    }

    public InvitedSpaceRes getInvitedSpace(String inviteCode) {
        SpaceInvited invited = getValidInvitation(inviteCode);
        Space space = invited.getSpace();

        return InvitedSpaceRes.of(space);
    }

    @Transactional
    public JoinSpaceRes joinSpace(Long userId, JoinSpaceReq request) {
        SpaceInvited invited = getValidInvitationWithLock(request.inviteKey());
        Space space = invited.getSpace();

        validateSpaceCapacity(space);
        User invitedUser = userComponent.getByIdAndDeleteFalse(userId);
        validateUserNotAlreadyJoined(space, invitedUser);

        SpaceMember.of(invitedUser, space);
        String landingUrl = generateLandingUrl(space.getId());

        spaceInvitedRepository.delete(invited);
        return JoinSpaceRes.of(space);
    }

    @Transactional
    public void deleteMember(Long ownerId, Long spaceId, Long targetMemberUserId) {
        Space space = spaceComponent.getByIdAndOwnerId(ownerId, spaceId, ApiErrorCode.PERMISSION_DENIED);
        validateUserIsNotOwner(ownerId, targetMemberUserId);

        SpaceMember target = findMemberByUserId(space, targetMemberUserId);
        space.removeMember(target);
    }

    public List<SpaceMembersRes> getSpaceMembers(Long userId, Long spaceId) {
        Space space = spaceComponent.getByIdAndUserIdWithMember(spaceId, userId, ApiErrorCode.PERMISSION_DENIED);
        List<SpaceMember> members = space.getMembers();

        return members.stream()
                .map(SpaceMembersRes::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void leaveSpace(Long userId, Long spaceId) {
        Space space = spaceComponent.getByIdAndUserIdWithMember(spaceId, userId, ApiErrorCode.PERMISSION_DENIED);
        SpaceMember me = space.getMember(userId);

        if (me.isOwner()) {
            throw new InvalidException(ApiErrorCode.OWNER_CANNOT_QUIT_SPACE);
        }

        space.removeMember(me);
    }

    // === Private Methods ===
    private SpaceInvited findOrCreateInvited(Space space, Long spaceId) {
        return spaceInvitedRepository.findBySpaceId(spaceId)
                .orElseGet(() -> {
                    SpaceInvited newInvited = SpaceInvited.of(space);
                    return spaceInvitedRepository.save(newInvited);
                });
    }

    private SpaceInvited getValidInvitation(String inviteKey) {
        return spaceInvitedRepository.findByInviteKeyAndDeleted(inviteKey)
                .orElseThrow(() -> new NotFoundException(ApiErrorCode.INVITATION_NOT_FOUND));
    }

    private SpaceInvited getValidInvitationWithLock(String inviteKey) {
        return spaceInvitedRepository.findByInviteKeyAndDeletedWithLock(inviteKey)
                .orElseThrow(() -> new NotFoundException(ApiErrorCode.INVITATION_NOT_FOUND));
    }

    private void validateSpaceCapacity(Space space) {
        if (space.getMembers().size() >= MAX_MEMBERS) {
            throw new InvalidException(ApiErrorCode.SPACE_MEMBER_LIMIT_EXCEEDED);
        }
    }

    private void validateUserNotAlreadyJoined(Space space, User user) {
        boolean alreadyJoined = space.getMembers().stream()
                .anyMatch(member -> member.getUser().equals(user));

        if (alreadyJoined) {
            throw new ConflictException(ApiErrorCode.SPACE_MEMBER_ALREADY_JOINED);
        }
    }

    private void validateUserIsNotOwner(Long ownerId, Long targetMemberUserId) {
        if (ownerId.equals(targetMemberUserId)) {
            throw new InvalidException(ApiErrorCode.OWNER_CANNOT_QUIT_SPACE);
        }
    }

    private SpaceMember findMemberByUserId(Space space, Long userId) {
        return space.getMembers().stream()
                .filter(m -> m.getUser().getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(ApiErrorCode.SPACE_MEMBER_NOT_FOUND));
    }

    private String generateLandingUrl(Long spaceId) {
        return baseUrl + "/" + SPACE_BASE_PATH + "/" + spaceId;
    }

    private String generateInvitationUrl(String inviteKey) {
        return baseUrl + "/" + invitationPath + "/" + inviteKey;
    }
}
