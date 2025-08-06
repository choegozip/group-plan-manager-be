package com.groupplanmanagerbe.presentation.space.dto.response.space;

import com.groupplanmanagerbe.domain.space.entity.Space;
import com.groupplanmanagerbe.domain.space.entity.SpaceMember;
import com.groupplanmanagerbe.domain.user.entity.User;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Builder
public record SpacesRes(
        Long id,
        String name,
        String profileImageKey,
        boolean isOwner,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<SpaceMemberInfo> member
) {
    public static SpacesRes of(Space space, boolean isOwner) {
        List<SpaceMemberInfo> member = space.getMembers().stream()
                .map(SpaceMemberInfo::of)
                .toList();
        return SpacesRes.builder()
                .id(space.getId())
                .name(space.getName())
                .profileImageKey(space.getProfileImageKey())
                .isOwner(isOwner)
                .createdAt(space.getCreatedAt())
                .updatedAt(space.getUpdatedAt())
                .member(member)
                .build();
    }

    @Builder
    public record SpaceMemberInfo(
        Long id,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        MemberUserInfo user
    ) {
        public static SpaceMemberInfo of(SpaceMember member) {
            return SpaceMemberInfo.builder()
                    .id(member.getId())
                    .createdAt(member.getCreatedAt())
                    .updatedAt(member.getUpdatedAt())
                    .user(MemberUserInfo.of(member.getUser()))
                    .build();
        }
    }

    @Builder
    public record MemberUserInfo(
            Long id,
            String nickname,
            String profileImageKey
    ) {
        public static MemberUserInfo of(User user) {
            return MemberUserInfo.builder()
                    .id(user.getId())
                    .nickname(user.getNickname())
                    .profileImageKey(user.getProfileImageKey())
                    .build();
        }
    }
}
