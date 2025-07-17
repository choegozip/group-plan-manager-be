package com.groupplanmanagerbe.global.exception.custom;

import com.groupplanmanagerbe.global.common.enums.ApiErrorCode;

public class DuplicateException extends CustomRuntimeException {
    public DuplicateException(ApiErrorCode errorCode) {
        super(errorCode);
    }
}
