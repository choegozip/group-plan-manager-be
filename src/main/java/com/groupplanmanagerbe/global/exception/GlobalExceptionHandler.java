package com.groupplanmanagerbe.global.exception;

import com.groupplanmanagerbe.global.common.enums.ApiErrorCode;
import com.groupplanmanagerbe.global.common.response.ApiErrorRes;
import com.groupplanmanagerbe.global.exception.custom.CustomRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomRuntimeException.class)
    protected ResponseEntity<ApiErrorRes> handleCustomException(
            final CustomRuntimeException e) {
        log.warn("Custom Exception: {}", e.getMessage());
        return createResponseEntity(ApiErrorRes.of(e.getErrorCode()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ApiErrorRes> handleMethodArgumentNotValidException(
            final MethodArgumentNotValidException e) {
        log.warn("벨리데이션 오류: {}", e.getMessage());
        ApiErrorRes response = new ApiErrorRes(HttpStatus.BAD_REQUEST,
                e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return createResponseEntity(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorRes> handleAccessDeniedException(
            AccessDeniedException e) {
        log.warn("접근 권한 없음: {}", e.getMessage());
        return createResponseEntity(ApiErrorRes.of(ApiErrorCode.FORBIDDEN_ACCESS));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiErrorRes> handleAuthenticationException(
            AuthenticationException e) {
        log.warn("인증 실패: {}", e.getMessage());

        ApiErrorCode errorCode = ApiErrorCode.UNAUTHORIZED_ACCESS;
        if (e instanceof BadCredentialsException) {
            errorCode = ApiErrorCode.LOGIN_FAILED;
        }

        return createResponseEntity(ApiErrorRes.of(errorCode));
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ApiErrorRes> handleIOException(IOException e){
        log.warn("IO Exception: {}", e.getMessage());
        ApiErrorRes errorResponse = new ApiErrorRes(HttpStatus.BAD_REQUEST, e.getMessage());
        return createResponseEntity(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ApiErrorRes> handleException(final Exception e) {
        log.error("Unexpected error occurred", e);  // 예상치 못한 에러는 ERROR 레벨로
        ApiErrorRes response = new ApiErrorRes(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        return createResponseEntity(response);
    }

    private ResponseEntity<ApiErrorRes> createResponseEntity(ApiErrorRes response) {
        return ResponseEntity.status(response.httpStatus()).body(response);
    }
}
