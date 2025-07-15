package com.groupplanmanagerbe.global.common.response;

import com.groupplanmanagerbe.global.common.enums.ApiErrorCode;
import org.springframework.http.HttpStatus;

public record ApiErrorRes(HttpStatus httpStatus, String message) {
    public static ApiErrorRes of(ApiErrorCode errorCode) {
        return new ApiErrorRes(errorCode.getHttpStatus(), errorCode.getMessage());
    }
}

