package com.groupplanmanagerbe.presentation.space.dto.response.spacemember;

import com.groupplanmanagerbe.domain.space.entity.Space;
import com.groupplanmanagerbe.domain.space.entity.SpaceMember;
import com.groupplanmanagerbe.domain.user.entity.User;
import lombok.Builder;

import java.util.List;

@Builder
public record InvitedSpaceRes(
        Long id,
        String name,
        List<MemberInfo> members
) {
    public static InvitedSpaceRes of(Space space) {
        List<MemberInfo> members = space.getMembers().stream()
                .filter(SpaceMember::isOwner)
                .map(MemberInfo::of)
                .toList();

        return InvitedSpaceRes.builder()
                .id(space.getId())
                .name(space.getName())
                .members(members)
                .build();
    }

    @Builder
    public record MemberInfo(
            boolean isOwner,
            UserInfo user
    ) {
        public static MemberInfo of(SpaceMember member) {
            return MemberInfo.builder()
                    .isOwner(member.isOwner())
                    .user(UserInfo.of(member.getUser()))
                    .build();
        }
    }

    @Builder
    public record UserInfo(
            Long id,
            String nickname
    ) {
        public static UserInfo of(User user) {
            return UserInfo.builder()
                    .id(user.getId())
                    .nickname(user.getNickname())
                    .build();
        }
    }
}
