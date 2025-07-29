package com.groupplanmanagerbe.presentation.user.dto.request;

import com.groupplanmanagerbe.presentation.user.dto.UserValidationConst;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateUserReq(

        @NotBlank(message = UserValidationConst.NICKNAME_BLANK_MESSAGE)
        @Size(
                min = UserValidationConst.NICKNAME_MIN,
                max = UserValidationConst.NICKNAME_MAX,
                message = UserValidationConst.NICKNAME_RANGE_MESSAGE)
        String nickname,

        @NotBlank(message = UserValidationConst.PASSWORD_BLANK_MESSAGE)
        @Size(min = UserValidationConst.PASSWORD_MIN, message = UserValidationConst.PASSWORD_MIN_MESSAGE)
        @Pattern(
                regexp = UserValidationConst.PASSWORD_REG,
                message = UserValidationConst.INVALID_PASSWORD_MESSAGE)
        String password,

        String profileImageKey
) {
}
