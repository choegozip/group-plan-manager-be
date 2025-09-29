package com.groupplanmanagerbe.global.common.enums;

import com.groupplanmanagerbe.global.exception.custom.InvalidException;
import lombok.Getter;

@Getter
public enum ManagerStatus {

    PENDING("manager.pending"),
    ACCEPT("manager.accept"),
    DENY("manager.deny"),
    DONE("manager.done");

    private final String message;

    ManagerStatus(String message) {
        this.message = message;
    }

    public static ManagerStatus of(String status) {
        if (status == null) return null;
        try {
            return ManagerStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidException(ApiErrorCode.STATUS_INVALID);
        }
    }
}