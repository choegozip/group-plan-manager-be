package com.groupplanmanagerbe.global.exception.custom;

import com.groupplanmanagerbe.global.common.enums.ApiErrorCode;

public class EmailException extends CustomRuntimeException {
    public EmailException(ApiErrorCode errorCode) {
        super(errorCode);
    }
}
