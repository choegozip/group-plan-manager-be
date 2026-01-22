package com.groupplanmanagerbe.presentation.email.dto.request;

import com.groupplanmanagerbe.presentation.user.dto.UserValidationConst;
import jakarta.validation.constraints.NotBlank;

public record MailVerifyReq(
        String email,
        @NotBlank(message = UserValidationConst.CODE_BLANK_MESSAGE)
        String code
) {
}
