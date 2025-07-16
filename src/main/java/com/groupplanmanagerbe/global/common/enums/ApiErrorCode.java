package com.groupplanmanagerbe.global.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ApiErrorCode {

    FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN, "00","접근금지"),
    INVALID_USER_ROLE(HttpStatus.BAD_REQUEST, "00","유효하지 않은 롤" ),

    // 유저 관련 익셉션
    DUPLICATED_EMAIL(HttpStatus.CONFLICT, "U001", "user.conflict"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"U002", "user.not_found"),
    ALREADY_DELETED(HttpStatus.BAD_REQUEST,"U003" ,"user.deleted" ),

    // 인증 관련 익셉션
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED,"A001", "로그인이 필요합니다."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "00","잘못된 아이디 또는 비밀번호입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "00","만료된 JWT 토큰입니다."),
    INVALID_TOKEN(HttpStatus.FORBIDDEN, "00","유효하지 않은 토큰입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "00","서버 내부 오류가 발생했습니다."),
   ;
    private final HttpStatus httpStatus;
    private final String code;
    private final String messageKey;
}