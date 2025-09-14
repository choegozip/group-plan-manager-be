package com.groupplanmanagerbe.presentation.auth.dto.request;

import com.groupplanmanagerbe.presentation.user.dto.UserValidationConst;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PasswordResetReq(
        String email,

        @NotBlank(message = UserValidationConst.PASSWORD_BLANK_MESSAGE)
        @Size(min = UserValidationConst.PASSWORD_MIN, message = UserValidationConst.PASSWORD_MIN_MESSAGE)
        @Pattern(
                regexp = UserValidationConst.PASSWORD_REG,
                message = UserValidationConst.INVALID_PASSWORD_MESSAGE)
        String password
) {
}
