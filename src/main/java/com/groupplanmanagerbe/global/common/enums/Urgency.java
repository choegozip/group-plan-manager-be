package com.groupplanmanagerbe.global.common.enums;

import com.groupplanmanagerbe.global.exception.custom.InvalidException;
import lombok.Getter;

@Getter
public enum Urgency {

    RED, YELLOW, GREEN;

    public static Urgency of(String urgency) {
        if (urgency == null) return null;
        try {
            return Urgency.valueOf(urgency.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidException(ApiErrorCode.URGENCY_INVALID);
        }
    }
}
