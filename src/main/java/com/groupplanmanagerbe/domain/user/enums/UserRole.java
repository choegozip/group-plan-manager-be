package com.groupplanmanagerbe.domain.user.enums;

import com.groupplanmanagerbe.global.common.enums.ApiErrorCode;
import com.groupplanmanagerbe.global.exception.custom.InvalidException;
import java.util.Arrays;

public enum UserRole {

    USER, ADMIN;

    public static UserRole of(String userRole) {
        return Arrays.stream(UserRole.values())
                .filter(r -> r.name().equalsIgnoreCase(userRole))
                .findFirst()
                .orElseThrow(() -> new InvalidException(ApiErrorCode.INVALID_USER_ROLE));
    }
}
