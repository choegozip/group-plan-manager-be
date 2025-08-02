package com.groupplanmanagerbe.presentation.user.dto.response;

import lombok.Builder;

@Builder
public record UserCreateRes(
        Long id
) {
    public static UserCreateRes of(Long id) {
        return UserCreateRes.builder()
                .id(id)
                .build();
    }
}
