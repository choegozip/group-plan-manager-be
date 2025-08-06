package com.groupplanmanagerbe.presentation.space.dto.response.space;

import com.groupplanmanagerbe.domain.space.entity.Space;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record SpaceRes(
        Long id,
        String nickname,
        String profileImageKey,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static SpaceRes from(Space space) {
        return SpaceRes.builder()
                .id(space.getId())
                .nickname(space.getName())
                .profileImageKey(space.getProfileImageKey())
                .createdAt(space.getCreatedAt())
                .updatedAt(space.getUpdatedAt())
                .build();
    }
}
