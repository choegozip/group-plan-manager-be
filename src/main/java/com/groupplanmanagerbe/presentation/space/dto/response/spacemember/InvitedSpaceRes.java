package com.groupplanmanagerbe.presentation.space.dto.response.spacemember;

import com.groupplanmanagerbe.domain.space.entity.Space;
import com.groupplanmanagerbe.domain.space.entity.SpaceMember;
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
            Long id,
            String nickname
    ) {
        public static MemberInfo of(SpaceMember member) {
            return MemberInfo.builder()
                    .isOwner(member.isOwner())
                    .id(member.getUser().getId())
                    .nickname(member.getUser().getNickname())
                    .build();
        }
    }
}
