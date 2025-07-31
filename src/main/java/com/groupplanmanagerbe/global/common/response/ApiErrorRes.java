package com.groupplanmanagerbe.global.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiErrorRes<T>(String code, String message, T data) {

    public static <T> ApiErrorRes<T> of(String code, String message) {
        return new ApiErrorRes<T>(code, message, null);
    }
}

