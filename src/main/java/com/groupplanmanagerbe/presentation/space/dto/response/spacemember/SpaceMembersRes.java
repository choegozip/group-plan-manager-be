package com.groupplanmanagerbe.presentation.space.dto.response.spacemember;

import com.groupplanmanagerbe.domain.space.entity.SpaceMember;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record SpaceMembersRes(
        Long id,
        String nickname,
        boolean isOwner,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static SpaceMembersRes from(SpaceMember member) {
        return SpaceMembersRes.builder()
                .id(member.getUser().getId())
                .nickname(member.getUser().getNickname())
                .isOwner(member.isOwner())
                .createdAt(member.getCreatedAt())
                .updatedAt(member.getUpdatedAt())
                .build();
    }
}
