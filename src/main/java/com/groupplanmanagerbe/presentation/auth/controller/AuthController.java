package com.groupplanmanagerbe.presentation.auth.controller;

import com.groupplanmanagerbe.domain.auth.service.AuthService;
import com.groupplanmanagerbe.global.common.enums.ApiSuccessCode;
import com.groupplanmanagerbe.global.common.response.ApiSuccessRes;
import com.groupplanmanagerbe.global.security.model.AuthUser;
import com.groupplanmanagerbe.presentation.auth.dto.request.LoginReq;
import com.groupplanmanagerbe.presentation.auth.dto.response.LoginRes;
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
    public ResponseEntity<ApiSuccessRes<LoginRes>> login(
            @Valid @RequestBody LoginReq request
    ) {
        LoginRes response = authService.login(request);
        return ApiSuccessRes.success(ApiSuccessCode.SUCCESS_LOGIN, response);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiSuccessRes<Void>> logout(
            @AuthenticationPrincipal AuthUser authUser
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        authService.logout(authentication.getCredentials().toString());
        return ApiSuccessRes.success(ApiSuccessCode.SUCCESS_LOGOUT);
    }
}
