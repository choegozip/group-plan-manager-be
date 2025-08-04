package com.groupplanmanagerbe.presentation.tobuyitem.dto.response;

import lombok.Builder;

@Builder
public record ToBuyRes(
        Long id
) {
    public static ToBuyRes of(Long id) {
        return ToBuyRes.builder()
                .id(id)
                .build();
    }
}
