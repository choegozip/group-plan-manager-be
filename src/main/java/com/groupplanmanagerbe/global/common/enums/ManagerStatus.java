package com.groupplanmanagerbe.global.common.enums;

import com.groupplanmanagerbe.global.exception.custom.InvalidException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ManagerStatus {

    HELP, OK, SORRY, DONE;

    public static ManagerStatus of(String status) {
        return Arrays.stream(ManagerStatus.values())
                .filter(u -> u.name().equalsIgnoreCase(status))
                .findFirst()
                .orElseThrow(() -> new InvalidException(ApiErrorCode.STATUS_INVALID));
    }
}