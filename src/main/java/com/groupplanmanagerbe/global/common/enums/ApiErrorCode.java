package com.groupplanmanagerbe.global.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.parser.HttpParser;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ApiErrorCode {

    // 유저 관련 익셉션
    USER_DUPLICATED_EMAIL(HttpStatus.CONFLICT, "ERROR", "user.conflict"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"ERROR", "user.not_found"),
    USER_ALREADY_DELETED(HttpStatus.BAD_REQUEST, "ERROR", "user.deleted"),
    USER_INVALID_ROLE(HttpStatus.BAD_REQUEST, "ERROR", "user.invalid.role"),

    // 인증 관련 익셉션
    ACCESS_DENIED(HttpStatus.FORBIDDEN,"ERROR", "auth.forbidden"),
    AUTH_REQUIRED(HttpStatus.UNAUTHORIZED, "AUTH_REQUIRED", "auth.access.denied"),
    AUTH_UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "AUTH_REQUIRED", "auth.unauthorized"),
    AUTH_INVALID_PASSWORD(HttpStatus.BAD_REQUEST,"ERROR", "auth.invalid.password"),

    // 토큰 관련 익셉션
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "AUTH_REQUIRED", "token.expired"),
    REFRESH_TOKEN_UNSUPPORTED_FORMAT(HttpStatus.UNAUTHORIZED, "AUTH_REQUIRED","token.unsupported_format"),
    REFRESH_TOKEN_MALFORMED(HttpStatus.UNAUTHORIZED, "AUTH_REQUIRED","token.malformed"),
    REFRESH_TOKEN_INVALID_SIGNATURE(HttpStatus.UNAUTHORIZED, "AUTH_REQUIRED","token.invalid_signature"),
    REFRESH_TOKEN_EMPTY(HttpStatus.UNAUTHORIZED, "AUTH_REQUIRED","token.empty"),

    ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "REFRESH_REQUIRED","token.expired"),
    ACCESS_TOKEN_UNSUPPORTED_FORMAT(HttpStatus.UNAUTHORIZED, "REFRESH_REQUIRED", "token.unsupported_format"),
    ACCESS_TOKEN_MALFORMED(HttpStatus.UNAUTHORIZED, "REFRESH_REQUIRED","token.malformed"),
    ACCESS_TOKEN_INVALID_SIGNATURE(HttpStatus.UNAUTHORIZED, "REFRESH_REQUIRED","token.invalid_signature"),
    ACCESS_TOKEN_EMPTY(HttpStatus.UNAUTHORIZED, "REFRESH_REQUIRED","token.empty"),

    TOKEN_BLACKLISTED(HttpStatus.UNAUTHORIZED,"AUTH_REQUIRED", "token.blacklisted"),
    TOKEN_MISSING_MEMBER_ID(HttpStatus.UNAUTHORIZED, "AUTH_REQUIRED", "token.missing_member_id"),
    TOKEN_INVALID_MEMBER_ID(HttpStatus.UNAUTHORIZED, "AUTH_REQUIRED", "token.invalid_member_id"),
    TOKEN_MISSING_ROLE(HttpStatus.UNAUTHORIZED, "AUTH_REQUIRED", "token.missing_role"),
    TOKEN_INVALID_ROLE(HttpStatus.UNAUTHORIZED, "AUTH_REQUIRED", "token.invalid_role"),
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "AUTH_REQUIRED", "token.not.found"),

    // JWT 시크릿 키 관련
    JWT_SECRET_KEY_EMPTY(HttpStatus.INTERNAL_SERVER_ERROR,"ERROR", "jwt.secret.key.empty"),
    JWT_SECRET_KEY_INVALID(HttpStatus.INTERNAL_SERVER_ERROR,"ERROR", "jwt.secret.key.invalid"),

    // 스페이스
    SPACE_NOT_FOUND(HttpStatus.BAD_REQUEST,"ERROR", "space.not.found"),
    INVITATION_NOT_FOUND(HttpStatus.NOT_FOUND,"ERROR", "invitation.not.found"),
    SPACE_MEMBER_ALREADY_JOINED(HttpStatus.CONFLICT,"ERROR", "space.member.conflict"),
    SPACE_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND,"ERROR", "space.member.not.found"),
    OWNER_CANNOT_QUIT_SPACE(HttpStatus.BAD_REQUEST,"ERROR", "space.owner.cannot.remove.self"),
    SPACE_MEMBER_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST,"ERROR","space.member.limit.exceeded"),
    OWNER_NOT_FOUND(HttpStatus.NOT_FOUND, "ERROR", "owner.not.found"),
    SPACE_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "ERROR", "space.limit.exceeded"),

    // 살 것 & 할 것
    URGENCY_INVALID(HttpStatus.BAD_REQUEST, "ERROR", "urgency.invalid"),
    INVALID_SPACE_ID(HttpStatus.BAD_REQUEST, "ERROR", "space.id.invalid"),
    MANAGER_NOT_FOUND(HttpStatus.NOT_FOUND, "ERROR", "manager.not.found"),
    STATUS_INVALID(HttpStatus.BAD_REQUEST, "ERROR", "manager.status.invalid"),
    TO_BUY_NOT_FOUND(HttpStatus.NOT_FOUND, "ERROR", "to.buy.not.found"),
    INVALID_TO_BUY_ID(HttpStatus.BAD_REQUEST, "ERROR", "to.buy.id.invalid"),
    TO_DO_NOT_FOUND(HttpStatus.NOT_FOUND, "ERROR", "to.do.not.found"),
    INVALID_TO_DO_ID(HttpStatus.BAD_REQUEST, "ERROR", "to.do.id.invalid"),

    // 코멘트
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "ERROR", "comment.not.found"),

    // 공통
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"ERROR", "server.internal.error"),
    PERMISSION_DENIED(HttpStatus.FORBIDDEN,"ERROR", "permission.denied"),
    INVALID_DATE_FORMAT(HttpStatus.BAD_REQUEST, "ERROR", "invalid.date.format"),
    INVALID_DATE(HttpStatus.BAD_REQUEST, "ERROR", "invalid.date"),
    SORT_DIRECTION_INVALID(HttpStatus.BAD_REQUEST, "ERROR", "sort_direction_invalid");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}