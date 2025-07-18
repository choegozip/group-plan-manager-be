package com.groupplanmanagerbe.global.exception.custom;

import com.groupplanmanagerbe.global.common.enums.ApiErrorCode;

public class JwtTokenException extends CustomRuntimeException{
    public JwtTokenException(ApiErrorCode errorCode) {
        super(errorCode);
    }
}
