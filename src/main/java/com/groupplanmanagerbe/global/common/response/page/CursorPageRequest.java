package com.groupplanmanagerbe.global.common.response.page;

import com.groupplanmanagerbe.global.common.enums.SortDirection;
import com.groupplanmanagerbe.global.common.enums.Urgency;
import lombok.Builder;

@Builder
public record CursorPageRequest(
        Long cursor,
        int size,
        String sortBy,
        String direction,
        Long managerId,
        String urgency
) {
    public static CursorPageRequest of(Long cursor, int size, String direction, Long managerId, String urgency) {
        return CursorPageRequest.builder()
                .cursor(cursor)
                .size(size)
                .sortBy("createdAt")
                .direction(direction != null ? SortDirection.of(direction).toString() : null)
                .managerId(managerId)
                .urgency(urgency != null ? Urgency.of(urgency).toString() : null)
                .build();
    }
}
