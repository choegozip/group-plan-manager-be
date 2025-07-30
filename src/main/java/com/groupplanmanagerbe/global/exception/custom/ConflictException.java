package com.groupplanmanagerbe.global.exception.custom;

import com.groupplanmanagerbe.global.common.enums.ApiErrorCode;

public class ConflictException extends CustomRuntimeException {
    public ConflictException(ApiErrorCode errorCode) {
        super(errorCode);
    }
}
