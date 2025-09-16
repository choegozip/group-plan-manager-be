package com.groupplanmanagerbe.global.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.groupplanmanagerbe.global.common.enums.ApiSuccessCode;
import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiSuccessRes<T>(String code, String message, T data) {

    public static <T> ApiSuccessRes<T> of(String code, String message, T data) {
        return ApiSuccessRes.<T>builder()
                .code(code)
                .message(message)
                .data(data)
                .build();
    }

    // 200 OK
    public static <T> ResponseEntity<ApiSuccessRes<T>> success(ApiSuccessCode successCode, T data) {
        return ResponseEntity.ok(new ApiSuccessRes<>(successCode.getCode(), successCode.getMessage(), data));
    }

    public static <T> ResponseEntity<ApiSuccessRes<T>> success(ApiSuccessCode successCode) {
        return ResponseEntity.ok(new ApiSuccessRes<>(successCode.getCode(), successCode.getMessage(), null));
    }

    public static <T> ApiSuccessRes<T> ok(ApiSuccessCode successCode, T data) {
        return ApiSuccessRes.of(successCode.getCode(), successCode.getMessage(), data);
    }

    // 201 CREATED
    public static <T> ResponseEntity<ApiSuccessRes<T>> created(ApiSuccessCode successCode, T data) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiSuccessRes<>(successCode.getCode(), successCode.getMessage(), data));
    }

    public static <T> ResponseEntity<ApiSuccessRes<T>> created(ApiSuccessCode successCode) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiSuccessRes<>(successCode.getCode(), successCode.getMessage(), null));
    }
}
