package com.groupplanmanagerbe.global.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
@Getter
@RequiredArgsConstructor
public enum ApiSuccessCode {
    // USER
    SUCCESS_SIGNUP("success","user.signup.success"),
    SUCCESS_GET_USER("success", "user.get.success"),
    SUCCESS_UPDATE_USER("success", "user.update.success"),
    SUCCESS_DELETE_USER("success", "user.delete.success")
    ;
    private final String code;
    private final String message;
}
