package com.groupplanmanagerbe.global.exception;

import com.groupplanmanagerbe.global.common.enums.ApiErrorCode;
import com.groupplanmanagerbe.global.common.response.ApiErrorRes;
import com.groupplanmanagerbe.global.exception.custom.CustomRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(CustomRuntimeException.class)
    protected ResponseEntity<ApiErrorRes> handleCustomException(
            final CustomRuntimeException e) {
        log.warn("예외 발생: {}", e.getMessage());
        String localizedMessage = messageSource.getMessage(
                e.getErrorCode().getMessageKey(), null, LocaleContextHolder.getLocale());
        return createResponseEntity(ApiErrorRes.of(e.getErrorCode().getHttpStatus(),localizedMessage));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ApiErrorRes> handleMethodArgumentNotValidException(
            final MethodArgumentNotValidException e) {
        log.warn("벨리데이션 오류: {}", e.getMessage());
        String defaultMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        String localizedMessage =
                messageSource.getMessage(defaultMessage, null, defaultMessage, LocaleContextHolder.getLocale());
        return createResponseEntity(ApiErrorRes.of(HttpStatus.BAD_REQUEST, localizedMessage));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorRes> handleAccessDeniedException(
            AccessDeniedException e) {
        log.warn("접근 권한 없음: {}", e.getMessage());
        String localizedMessage = messageSource.getMessage(
                ApiErrorCode.FORBIDDEN_ACCESS.getMessageKey(), null, LocaleContextHolder.getLocale());
        return createResponseEntity(ApiErrorRes.of(HttpStatus.FORBIDDEN, localizedMessage));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiErrorRes> handleAuthenticationException(
            AuthenticationException e) {
        log.warn("인증 실패: {}", e.getMessage());
        ApiErrorCode errorCode = ApiErrorCode.UNAUTHORIZED_ACCESS;
        if (e instanceof BadCredentialsException) {
            errorCode = ApiErrorCode.LOGIN_FAILED;
        }
        String localizedMessage = messageSource.getMessage(
                errorCode.getMessageKey(), null, LocaleContextHolder.getLocale());
        return createResponseEntity(ApiErrorRes.of(errorCode.getHttpStatus() ,localizedMessage));
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ApiErrorRes> handleIOException(IOException e){
        log.warn("IO Exception: {}", e.getMessage());
        String localizedMessage = messageSource.getMessage(
                ApiErrorCode.INTERNAL_SERVER_ERROR.getMessageKey(), null, LocaleContextHolder.getLocale());
        return createResponseEntity(ApiErrorRes.of(HttpStatus.INTERNAL_SERVER_ERROR, localizedMessage));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ApiErrorRes> handleException(final Exception e) {
        log.error("Unexpected error occurred", e);  // 예상치 못한 에러는 ERROR 레벨로
        String localizedMessage = messageSource.getMessage(
                ApiErrorCode.INTERNAL_SERVER_ERROR.getMessageKey(), null, LocaleContextHolder.getLocale());
        return createResponseEntity(ApiErrorRes.of(HttpStatus.INTERNAL_SERVER_ERROR, localizedMessage));
    }

    private ResponseEntity<ApiErrorRes> createResponseEntity(ApiErrorRes response) {
        return ResponseEntity.status(response.httpStatus()).body(response);
    }
}
