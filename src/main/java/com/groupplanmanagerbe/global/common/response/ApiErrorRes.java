package com.groupplanmanagerbe.global.common.response;

import org.springframework.http.HttpStatus;

public record ApiErrorRes(HttpStatus httpStatus, String messageKey) {

    public static ApiErrorRes of(HttpStatus httpStatus, String message) {
        return new ApiErrorRes(httpStatus, message);
    }
}

