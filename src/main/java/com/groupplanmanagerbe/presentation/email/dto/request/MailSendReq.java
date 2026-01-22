package com.groupplanmanagerbe.presentation.email.dto.request;

import com.groupplanmanagerbe.presentation.user.dto.UserValidationConst;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record MailSendReq(
        @NotBlank(message = UserValidationConst.EMAIL_BLANK_MESSAGE)
        @Pattern(
                regexp = UserValidationConst.EMAIL_REG,
                message = UserValidationConst.INVALID_EMAIL_MESSAGE)
        String email
) {
}
