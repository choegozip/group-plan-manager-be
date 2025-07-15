package com.groupplanmanagerbe.global.exception.custom;

import com.groupplanmanagerbe.global.common.enums.ApiErrorCode;

public class InvalidException extends CustomRuntimeException {
    public InvalidException(ApiErrorCode errorCode) {
        super(errorCode);
    }
}
