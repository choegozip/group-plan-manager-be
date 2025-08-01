package com.groupplanmanagerbe.global.common.enums;

import com.groupplanmanagerbe.global.exception.custom.InvalidException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Urgency {

    RED, YELLO, GREEN;

    public static Urgency of(String urgency) {
        return Arrays.stream(Urgency.values())
                .filter(u -> u.name().equalsIgnoreCase(urgency))
                .findFirst()
                .orElseThrow(() -> new InvalidException(ApiErrorCode.URGENCY_INVALID));
    }
}
