package com.groupplanmanagerbe.global.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
@Getter
@RequiredArgsConstructor
public enum ApiSuccessCode {
    // USER
    SUCCESS_SIGNUP("SUCCESS", "user.signup.success"),
    SUCCESS_USER_GET("SUCCESS", "user.get.success"),
    SUCCESS_USER_UPDATE("SUCCESS", "user.update.success"),
    SUCCESS_USER_DELETE("SUCCESS", "user.delete.success"),
    SUCCESS_LOGIN("SUCCESS", "user.login.success"),
    SUCCESS_LOGOUT("SUCCESS", "user.logout.success"),
    SUCCESS_REFRESH_TOKEN("SUCCESS", "user.refresh.token"),

    // SPACE
    SUCCESS_SPACE_CREATE("SUCCESS", "space.create.success"),
    SUCCESS_SPACE_UPDATE("SUCCESS", "space.update.success"),
    SUCCESS_SPACE_DELETE("SUCCESS", "space.delete.success"),
    SUCCESS_SPACES_GET("SUCCESS", "spaces.get.success"),
    SUCCESS_SPACE_GET("SUCCESS", "space.get.success"),
    SUCCESS_SPACE_INVITE("SUCCESS", "space.invite.success"),
    SUCCESS_JOIN_SPACE("SUCCESS", "space.join.success"),
    SUCCESS_DELETE_SPACE_MEMBER("SUCCESS", "space.delete.member.success"),
    SUCCESS_GET_SPACE_MEMBERS("SUCCESS", "space.get.members.success"),
    SUCCESS_LEAVE_SPACE("SUCCESS", "space.leave.space"),
    SUCCESS_GET_INVITED_SPACE("SUCCESS", "invited_space_get_success"),

    // TO BUY
    SUCCESS_TO_BUY_CREATE("SUCCESS", "to.buy.create.success"),
    SUCCESS_TO_BUY_UPDATE("SUCCESS", "to.buy.update.success"),
    SUCCESS_TO_BUY_DELETE("SUCCESS", "to.buy.delete.success"),
    SUCCESS_UPDATE_MANAGER_STATUS("SUCCESS", "manager.status.update.success"),
    SUCCESS_GET_TO_BUY_LIST("SUCCESS", "to.buy.list.get.success"),
    SUCCESS_GET_TO_BUY("SUCCESS", "to.buy.get.success")
    ;

    private final String code;
    private final String message;
}
