package com.groupplanmanagerbe.domain.user.enums;

import com.groupplanmanagerbe.global.common.enums.ApiErrorCode;
import com.groupplanmanagerbe.global.common.enums.ManagerStatus;
import com.groupplanmanagerbe.global.exception.custom.InvalidException;
import java.util.Arrays;

public enum UserRole {

    USER, ADMIN;

    public static UserRole of(String userRole) {
        if (userRole == null) return null;
        try {
            return UserRole.valueOf(userRole.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidException(ApiErrorCode.USER_INVALID_ROLE);
        }
    }
}
