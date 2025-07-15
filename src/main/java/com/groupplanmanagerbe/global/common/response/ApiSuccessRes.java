package com.groupplanmanagerbe.global.common.response;

import com.groupplanmanagerbe.global.common.enums.ApiSuccessCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public record ApiSuccessRes<T>(String code, String message, T data) {

    // 200 OK
    public static <T> ResponseEntity<ApiSuccessRes<T>> success(ApiSuccessCode successCode, T data) {
        return ResponseEntity.ok(new ApiSuccessRes<>(successCode.getCode(), successCode.getMessage(), data));
    }

    public static <T> ResponseEntity<ApiSuccessRes<T>> success(ApiSuccessCode successCode) {
        return ResponseEntity.ok(new ApiSuccessRes<>(successCode.getCode(), successCode.getMessage(), null));
    }

    // 201 CREATED
    public static <T> ResponseEntity<ApiSuccessRes<T>> created(ApiSuccessCode successCode) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiSuccessRes<>(successCode.getCode(), successCode.getMessage(), null));
    }

    // 204 NO CONTENT
    public static <T> ResponseEntity<ApiSuccessRes<Void>> noContent(ApiSuccessCode successCode) {
        return ResponseEntity.noContent().build();
    }
}
