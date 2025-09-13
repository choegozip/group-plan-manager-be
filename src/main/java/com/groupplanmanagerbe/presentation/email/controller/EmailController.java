package com.groupplanmanagerbe.presentation.email.controller;

import com.groupplanmanagerbe.domain.mail.service.EmailService;
import com.groupplanmanagerbe.global.common.enums.ApiSuccessCode;
import com.groupplanmanagerbe.global.common.response.ApiSuccessRes;
import com.groupplanmanagerbe.presentation.email.dto.request.MailSendReq;
import com.groupplanmanagerbe.presentation.email.dto.request.MailVerifyReq;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send-email")
    public ResponseEntity<ApiSuccessRes<Void>> sendEmail(
            @Valid @RequestBody MailSendReq request
    ) {
        emailService.sendCodeToEmail(request.email());
        return ApiSuccessRes.success(ApiSuccessCode.SUCCESS_SEND_EMAIL);
    }

    @PostMapping("/verify-email")
    public ResponseEntity<ApiSuccessRes<Void>> verifyCode(
            @Valid @RequestBody MailVerifyReq request
    ) {
        emailService.verifyCode(request.email(), request.code());
        return ApiSuccessRes.success(ApiSuccessCode.SUCCESS_VERIFY_EMAIL);
    }
}
