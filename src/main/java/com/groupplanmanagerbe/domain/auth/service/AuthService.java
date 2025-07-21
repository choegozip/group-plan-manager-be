package com.groupplanmanagerbe.domain.auth.service;

import com.groupplanmanagerbe.domain.user.entity.User;
import com.groupplanmanagerbe.domain.user.service.UserComponent;
import com.groupplanmanagerbe.global.common.enums.ApiErrorCode;
import com.groupplanmanagerbe.global.exception.custom.InvalidException;
import com.groupplanmanagerbe.global.security.model.JwtSecurityProperties;
import com.groupplanmanagerbe.global.security.token.JwtUtil;
import com.groupplanmanagerbe.presentation.auth.dto.request.LoginReq;
import com.groupplanmanagerbe.presentation.auth.dto.response.LoginRes;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final BlackListTokenService blackListTokenService;
    private final RefreshTokenService refreshTokenService;
    private final UserComponent userComponent;
    private final JwtUtil jwtUtil;
    private final JwtSecurityProperties jwtSecurityProperties;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public LoginRes login(LoginReq request) {
        User savedUser = userComponent.getByEmail(request.email());
        if (!passwordEncoder.matches(request.password(), savedUser.getPassword())) {
            throw new InvalidException(ApiErrorCode.AUTH_INVALID_PASSWORD);
        }
        String accessToken = jwtUtil.createAccessToken(savedUser.getId(), savedUser.getRole());
        String refreshToken = jwtUtil.createRefreshToken(savedUser.getId());

        refreshTokenService.create(savedUser, refreshToken);

        return LoginRes.of(accessToken, refreshToken);
    }

    @Transactional
    public void logout(String token) {
        Claims claims = jwtUtil.parseClaims(token);
        Duration remaining = jwtUtil.getRemainingValidity(claims);
        long mills = remaining.toMillis();

        if (mills > 0) {
            blackListTokenService.save(token, mills);
            refreshTokenService.delete(Long.valueOf(claims.getSubject()));
        }
    }
}
