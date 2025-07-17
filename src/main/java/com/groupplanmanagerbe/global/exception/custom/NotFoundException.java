package com.groupplanmanagerbe.global.exception.custom;

import com.groupplanmanagerbe.global.common.enums.ApiErrorCode;

public class NotFoundException extends CustomRuntimeException {
    public NotFoundException(ApiErrorCode errorCode) {
        super(errorCode);
    }
}
