package com.groupplanmanagerbe.global.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ApiErrorCode {

    // 유저 관련 익셉션
    USER_DUPLICATED_EMAIL(HttpStatus.CONFLICT, "USER_DUPLICATED_EMAIL", "user.conflict"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "user.not_found"),
    USER_ALREADY_DELETED(HttpStatus.BAD_REQUEST, "USER_ALREADY_DELETED", "user.deleted"),
    USER_INVALID_ROLE(HttpStatus.BAD_REQUEST, "USER_INVALID_ROLE", "user.invalid.role"),

    // 인증 관련 익셉션
    AUTH_FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN, "AUTH_FORBIDDEN_ACCESS", "auth.forbidden"),
    AUTH_ACCESS_DENIED(HttpStatus.UNAUTHORIZED, "AUTH_ACCESS_DENIED", "auth.access.denied"),
    AUTH_UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "AUTH_UNAUTHORIZED_ACCESS", "auth.unauthorized"),
    AUTH_LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "AUTH_LOGIN_FAILED", "auth.login.failed"),
    AUTH_INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "AUTH_INVALID_PASSWORD", "auth.invalid.password"),

    // 토큰 관련 익셉션
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "TOKEN_EXPIRED", "token.expired"),
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "TOKEN_INVALID", "token.invalid"),
    TOKEN_UNSUPPORTED_FORMAT(HttpStatus.UNAUTHORIZED, "TOKEN_UNSUPPORTED_FORMAT", "token.unsupported_format"),
    TOKEN_MALFORMED(HttpStatus.UNAUTHORIZED, "TOKEN_MALFORMED", "token.malformed"),
    TOKEN_INVALID_SIGNATURE(HttpStatus.UNAUTHORIZED, "TOKEN_INVALID_SIGNATURE", "token.invalid_signature"),
    TOKEN_EMPTY(HttpStatus.UNAUTHORIZED, "TOKEN_EMPTY", "token.empty"),
    TOKEN_BLACKLISTED(HttpStatus.UNAUTHORIZED, "TOKEN_BLACKLISTED", "token.blacklisted"),
    TOKEN_MISSING_MEMBER_ID(HttpStatus.UNAUTHORIZED, "TOKEN_MISSING_MEMBER_ID", "token.missing_member_id"),
    TOKEN_INVALID_MEMBER_ID(HttpStatus.UNAUTHORIZED, "TOKEN_INVALID_MEMBER_ID", "token.invalid_member_id"),
    TOKEN_MISSING_ROLE(HttpStatus.UNAUTHORIZED, "TOKEN_MISSING_ROLE", "token.missing_role"),
    TOKEN_INVALID_ROLE(HttpStatus.UNAUTHORIZED, "TOKEN_INVALID_ROLE", "token.invalid_role"),
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "TOKEN_NOT_FOUND", "token.not.found"),
    TOKEN_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "TOKEN_UNAUTHORIZED", "token.unauthorized"),

    // JWT 시크릿 키 관련
    JWT_SECRET_KEY_EMPTY(HttpStatus.INTERNAL_SERVER_ERROR, "JWT_SECRET_KEY_EMPTY", "jwt.secret.key.empty"),
    JWT_SECRET_KEY_INVALID(HttpStatus.INTERNAL_SERVER_ERROR, "JWT_SECRET_KEY_INVALID", "jwt.secret.key.invalid"),

    // 공통
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "server.internal.error"),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}