package com.groupplanmanagerbe.global.common.enums;

import com.groupplanmanagerbe.global.exception.custom.InvalidException;
import lombok.Getter;

@Getter
public enum ManagerStatus {

    HELP, OK, SORRY, DONE;

    public static ManagerStatus of(String status) {
        if (status == null) return null;
        try {
            return ManagerStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidException(ApiErrorCode.STATUS_INVALID);
        }
    }
}