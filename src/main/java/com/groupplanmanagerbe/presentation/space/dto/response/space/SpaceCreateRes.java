package com.groupplanmanagerbe.presentation.space.dto.response.space;

import lombok.Builder;

@Builder
public record SpaceCreateRes(
        Long id
) {
    public static SpaceCreateRes of(Long id) {
        return SpaceCreateRes.builder()
                .id(id)
                .build();
    }
}
