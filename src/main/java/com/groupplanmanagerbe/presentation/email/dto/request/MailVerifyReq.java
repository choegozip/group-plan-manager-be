package com.groupplanmanagerbe.presentation.email.dto.request;

public record MailVerifyReq(
        String email,
        String code
) {
}
