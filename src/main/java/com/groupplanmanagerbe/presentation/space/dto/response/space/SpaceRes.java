package com.groupplanmanagerbe.presentation.space.dto.response.space;

import com.groupplanmanagerbe.domain.space.entity.Space;
import com.groupplanmanagerbe.domain.space.entity.SpaceMember;
import com.groupplanmanagerbe.domain.user.entity.User;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record SpaceRes(
        Long id,
        String nickname,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<SpaceMemberInfo> member
) {
    public static SpaceRes from(Space space) {
        List<SpaceMemberInfo> members = space.getMembers().stream()
                .map(SpaceMemberInfo::of)
                .toList();

        return SpaceRes.builder()
                .id(space.getId())
                .nickname(space.getName())
                .createdAt(space.getCreatedAt())
                .updatedAt(space.getUpdatedAt())
                .member(members)
                .build();
    }

    @Builder
    public record SpaceMemberInfo(
            Long id,
            String nickname,
            String profileImageKey
    ) {
        public static SpaceMemberInfo of(SpaceMember member) {
            User user = member.getUser();
            return SpaceMemberInfo.builder()
                    .id(user.getId())
                    .nickname(user.getNickname())
                    .profileImageKey(user.getProfileImageKey())
                    .build();
        }
    }
}
