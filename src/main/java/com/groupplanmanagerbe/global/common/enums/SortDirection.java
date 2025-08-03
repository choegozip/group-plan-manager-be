package com.groupplanmanagerbe.global.common.enums;

import com.groupplanmanagerbe.global.exception.custom.InvalidException;

public enum SortDirection {

    ASC, DESC;

    public static SortDirection of(String direction) {
        if (direction == null) return null;
        try {
            return SortDirection.valueOf(direction.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidException(ApiErrorCode.SORT_DIRECTION_INVALID);
        }
    }
}
