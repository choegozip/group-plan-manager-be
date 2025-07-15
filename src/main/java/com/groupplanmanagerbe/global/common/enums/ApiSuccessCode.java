package com.groupplanmanagerbe.global.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
@Getter
@RequiredArgsConstructor
public enum ApiSuccessCode {
    // USER
    SUCCESS_SIGNUP("success","회원가입 완료"),
    SUCCESS_GET_USER("success", "회원정보 조회 완료"),
    SUCCESS_UPDATE_USER("success", "회원정보 수정 완료"),
    SUCCESS_DELETE_USER("success", "탈퇴 완료")
    ;
    private final String code;
    private final String message;
}
