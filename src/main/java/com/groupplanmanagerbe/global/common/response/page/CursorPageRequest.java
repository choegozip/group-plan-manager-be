package com.groupplanmanagerbe.global.common.response.page;

import lombok.Builder;
import org.hibernate.query.SortDirection;

@Builder
public record CursorPageRequest(
        String cursor,
        int size,
        String sortBy,
        SortDirection direction
) {
    public static CursorPageRequest of(String cursor, int size, SortDirection direction) {
        return new CursorPageRequest(
                cursor,
                size,
                "updatedAt",
                direction != null ? direction : SortDirection.DESCENDING
        );
    }

    public boolean isFirstPage() {
        return cursor == null;
    }
}
