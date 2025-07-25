package com.groupplanmanagerbe.global.exception.custom;

import com.groupplanmanagerbe.global.common.enums.ApiErrorCode;

public class UnAuthorizedException extends CustomRuntimeException {
    public UnAuthorizedException(ApiErrorCode errorCode) {
        super(errorCode);
    }
}
