package com.groupplanmanagerbe.global.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
@Getter
@RequiredArgsConstructor
public enum ApiSuccessCode {
    // USER
    SUCCESS_SIGNUP("SUCCESS_SIGNUP","user.signup.success"),
    SUCCESS_USER_GET("SUCCESS_USER_GET", "user.get.success"),
    SUCCESS_USER_UPDATE("SUCCESS_USER_UPDATE", "user.update.success"),
    SUCCESS_USER_DELETE("SUCCESS_USER_DELETE", "user.delete.success"),
    SUCCESS_LOGIN("SUCCESS_LOGIN", "user.login.success"),
    SUCCESS_LOGOUT("SUCCESS_LOGOUT", "user.logout.success"),
    SUCCESS_REFRESH_TOKEN("SUCCESS_REFRESH_TOKEN", "user.refresh.token" ),

    // SPACE
    SUCCESS_CREATE_SPACE("SUCCESS_CREATE_SPACE", "space.create.success" );

    private final String code;
    private final String message;
}
