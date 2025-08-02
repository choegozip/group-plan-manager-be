package com.groupplanmanagerbe.presentation.space.dto.response.space;

import com.groupplanmanagerbe.domain.space.entity.Space;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record SpacesRes(
        Long id,
        String name,
        String profileImageKey,
        boolean isOwner,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static SpacesRes of(Space space, boolean isOwner) {
        return SpacesRes.builder()
                .id(space.getId())
                .name(space.getName())
                .profileImageKey(space.getProfileImageKey())
                .isOwner(isOwner)
                .createdAt(space.getCreatedAt())
                .updatedAt(space.getUpdatedAt())
                .build();
    }
}
