package com.groupplanmanagerbe.presentation.auth.controller;

import com.groupplanmanagerbe.domain.auth.service.AuthService;
import com.groupplanmanagerbe.global.common.enums.ApiSuccessCode;
import com.groupplanmanagerbe.global.common.response.ApiSuccessRes;
import com.groupplanmanagerbe.global.security.model.AuthUser;
import com.groupplanmanagerbe.presentation.auth.dto.request.LoginReq;
import com.groupplanmanagerbe.presentation.auth.dto.request.PasswordResetReq;
import com.groupplanmanagerbe.presentation.auth.dto.request.RefreshTokenReq;
import com.groupplanmanagerbe.presentation.auth.dto.response.TokenRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiSuccessRes<TokenRes>> login(
            @Valid @RequestBody LoginReq request
    ) {
        TokenRes response = authService.login(request);
        return ApiSuccessRes.success(ApiSuccessCode.SUCCESS_LOGIN, response);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiSuccessRes<Void>> logout(
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        authService.logout(authentication.getCredentials().toString());
        return ApiSuccessRes.success(ApiSuccessCode.SUCCESS_LOGOUT);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiSuccessRes<TokenRes>> refreshAccessToken(
            @RequestBody RefreshTokenReq request
    ) {
        TokenRes response = authService.refreshAccessToken(request);
        return ApiSuccessRes.success(ApiSuccessCode.SUCCESS_REFRESH_TOKEN, response);
    }

    @PostMapping("/password-reset")
    public ResponseEntity<ApiSuccessRes<Void>> resetPassword(
            @Valid @RequestBody PasswordResetReq request
    ) {
        authService.resetPassword(request);
        return ApiSuccessRes.success(ApiSuccessCode.SUCCESS_PASSWORD_RESET);
    }
}
