package com.groupplanmanagerbe.presentation.social.controller;

import com.google.firebase.auth.FirebaseAuthException;
import com.groupplanmanagerbe.domain.social.service.SocialUserService;
import com.groupplanmanagerbe.global.common.enums.ApiSuccessCode;
import com.groupplanmanagerbe.global.common.enums.SocialProvider;
import com.groupplanmanagerbe.global.common.response.ApiSuccessRes;
import com.groupplanmanagerbe.presentation.auth.dto.response.TokenRes;
import com.groupplanmanagerbe.presentation.social.dto.request.SocialTokenReq;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/social")
@RequiredArgsConstructor
public class SocialController {

    private final SocialUserService socialUserService;

    @PostMapping("/{provider}")
    public ResponseEntity<ApiSuccessRes<TokenRes>> socialLogin(
            @RequestBody SocialTokenReq request,
            @PathVariable String provider
    ) throws FirebaseAuthException {
        TokenRes response = socialUserService.socialLogin(request.token(), SocialProvider.of(provider));
        return ApiSuccessRes.success(ApiSuccessCode.SUCCESS_SOCIAL_LOGIN, response);
    }
}
