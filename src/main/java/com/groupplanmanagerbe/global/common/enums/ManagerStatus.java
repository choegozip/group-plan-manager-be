package com.groupplanmanagerbe.global.common.enums;

import com.groupplanmanagerbe.global.exception.custom.InvalidException;
import lombok.Getter;

@Getter
public enum ManagerStatus {

    PENDING("아직 대기 상태예요\uD83D\uDE42"),
    ACCEPT("제가 처리할게요😎"),
    DENY("지금은 처리하기 어려워요 \uD83D\uDE22"),
    DONE("처리 완료했습니다\uD83E\uDD70");

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