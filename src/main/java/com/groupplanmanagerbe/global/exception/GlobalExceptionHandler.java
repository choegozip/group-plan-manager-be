package com.groupplanmanagerbe.global.exception;

import com.groupplanmanagerbe.global.common.enums.ApiErrorCode;
import com.groupplanmanagerbe.global.common.response.ApiErrorRes;
import com.groupplanmanagerbe.global.exception.custom.CustomRuntimeException;
import com.groupplanmanagerbe.global.message.MessageResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageResolver messageResolver;

    @ExceptionHandler(CustomRuntimeException.class)
    protected ResponseEntity<ApiErrorRes> handleCustomException(
            final CustomRuntimeException e) {
        String localeMessage = messageResolver.get(e.getErrorCode().getMessage());
        log.warn("예외 발생: {}", localeMessage);
        return createResponseEntity(
                e.getErrorCode().getHttpStatus(),
                ApiErrorRes.of(e.getErrorCode().getCode(), localeMessage));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ApiErrorRes> handleMethodArgumentNotValidException(
            final MethodArgumentNotValidException e) {
        log.warn("벨리데이션 오류: {}", e.getMessage());
        String localeMessage = messageResolver.get(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return createResponseEntity(
                HttpStatus.BAD_REQUEST, ApiErrorRes.of("ERROR", localeMessage));
    }

//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<ApiErrorRes> handleAccessDeniedException(
//            AccessDeniedException e) {
//        log.warn("접근 권한 없음: {}", e.getMessage());
//        String localeMessage = messageResolver.get(ApiErrorCode.ACCESS_DENIED.getMessage());
//        return createResponseEntity(
//                HttpStatus.FORBIDDEN, ApiErrorRes.of(ApiErrorCode.ACCESS_DENIED.getCode(), localeMessage));
//    }

//    @ExceptionHandler(AuthenticationException.class)
//    public ResponseEntity<ApiErrorRes> handleAuthenticationException(
//            AuthenticationException e) {
//        log.warn("인증 실패: {}", e.getMessage());
//        ApiErrorCode errorCode = ApiErrorCode.AUTH_REQUIRED;
//        if (e instanceof BadCredentialsException) {
//            errorCode = ApiErrorCode.AUTH_LOGIN_FAILED;
//        }
//        String localeMessage = messageResolver.get(errorCode.getMessage());
//        return createResponseEntity(errorCode.getHttpStatus(), ApiErrorRes.of(errorCode.getCode() ,localeMessage));
//    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ApiErrorRes> handleIOException(IOException e){
        log.warn("IO Exception: {}", e.getMessage());
        String localeMessage = messageResolver.get(ApiErrorCode.INTERNAL_SERVER_ERROR.getMessage());
        return createResponseEntity(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ApiErrorRes.of(ApiErrorCode.INVITATION_NOT_FOUND.getCode(), localeMessage));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ApiErrorRes> handleException(final Exception e) {
        log.error("예상치 못한 에러 발생", e);  // 예상치 못한 에러는 ERROR 레벨로
        String localeMessage = messageResolver.get(ApiErrorCode.INTERNAL_SERVER_ERROR.getMessage());
        return createResponseEntity(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ApiErrorRes.of(ApiErrorCode.INVITATION_NOT_FOUND.getCode(), localeMessage));
    }

    private ResponseEntity<ApiErrorRes> createResponseEntity(HttpStatus status, ApiErrorRes response) {
        return ResponseEntity.status(status).body(response);
    }
}
