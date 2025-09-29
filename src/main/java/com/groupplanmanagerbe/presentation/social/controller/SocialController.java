package com.groupplanmanagerbe.presentation.social.controller;

import com.google.firebase.auth.FirebaseAuthException;
import com.groupplanmanagerbe.domain.social.service.SocialUserService;
import com.groupplanmanagerbe.global.common.enums.ApiSuccessCode;
import com.groupplanmanagerbe.global.common.response.ApiSuccessRes;
import com.groupplanmanagerbe.presentation.auth.dto.response.TokenRes;
import com.groupplanmanagerbe.presentation.social.dto.IdTokenReq;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/social")
@RequiredArgsConstructor
public class SocialController {

    private final SocialUserService socialUserService;

    @PostMapping("/google")
    public ResponseEntity<ApiSuccessRes<TokenRes>> loginWithGoogle(
            @RequestBody IdTokenReq request
    ) throws FirebaseAuthException {
        TokenRes response = socialUserService.loginWithGoogle(request.idToken());
        return ApiSuccessRes.success(ApiSuccessCode.SUCCESS_GOOGLE_LOGIN, response);
    }
}
