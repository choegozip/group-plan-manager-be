package com.groupplanmanagerbe.presentation.auth.dto.request;

import com.groupplanmanagerbe.presentation.user.dto.UserValidationConst;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record LoginReq(
        @NotBlank(message = UserValidationConst.EMAIL_BLANK_MESSAGE)
        @Pattern(
                regexp = UserValidationConst.EMAIL_REG,
                message = UserValidationConst.INVALID_EMAIL_MESSAGE)
        String email,

        @NotBlank(message = UserValidationConst.PASSWORD_BLANK_MESSAGE)
        String password
) {
}
