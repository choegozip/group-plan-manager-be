package com.groupplanmanagerbe.presentation.space.dto.response;

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
}
