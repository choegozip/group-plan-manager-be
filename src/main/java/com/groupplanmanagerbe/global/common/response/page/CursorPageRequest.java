package com.groupplanmanagerbe.global.common.response.page;

import lombok.Builder;

@Builder
public record CursorPageRequest(
        Long cursor,
        int size,
        String direction
) {
    public static CursorPageRequest of(Long cursor, int size, String direction) {
        return CursorPageRequest.builder()
                .cursor(cursor)
                .size(size)
                .direction(direction)
                .build();
    }
}
