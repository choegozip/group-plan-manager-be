package com.groupplanmanagerbe.domain.auth.service;

import com.groupplanmanagerbe.domain.auth.entity.RefreshToken;
import com.groupplanmanagerbe.domain.mail.service.EmailService;
import com.groupplanmanagerbe.domain.user.entity.User;
import com.groupplanmanagerbe.domain.user.enums.UserRole;
import com.groupplanmanagerbe.domain.user.service.UserComponent;
import com.groupplanmanagerbe.global.common.enums.ApiErrorCode;
import com.groupplanmanagerbe.global.exception.custom.InvalidException;
import com.groupplanmanagerbe.global.exception.custom.NotFoundException;
import com.groupplanmanagerbe.global.exception.custom.UnAuthorizedException;
import com.groupplanmanagerbe.global.security.token.JwtUtil;
import com.groupplanmanagerbe.presentation.auth.dto.request.LoginReq;
import com.groupplanmanagerbe.presentation.auth.dto.request.PasswordResetReq;
import com.groupplanmanagerbe.presentation.auth.dto.request.RefreshTokenReq;
import com.groupplanmanagerbe.presentation.auth.dto.response.TokenRes;
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

    private final BlackListTokenService blackListService;
    private final RefreshTokenService refreshService;
    private final UserComponent userComponent;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Transactional
    public TokenRes login(LoginReq request) {
        User savedUser = userComponent.getByEmail(request.email());
        validatePassword(request, savedUser);

        String accessToken = jwtUtil.createAccessToken(savedUser.getId(), savedUser.getRole());
        String refreshToken = jwtUtil.createRefreshToken(savedUser.getId());
        createOrUpdateRefreshToken(savedUser, refreshToken);

        return TokenRes.of(accessToken, refreshToken);
    }

    @Transactional
    public void logout(String token) {
        Claims claims = jwtUtil.parseAccessToken(token);
        Duration remaining = jwtUtil.getRemainingValidity(claims);
        long mills = remaining.toMillis();

        if (mills > 0) {
            blackListService.save(token, mills);
            refreshService.delete(Long.valueOf(claims.getSubject()));
        }
    }

    @Transactional
    public TokenRes refreshAccessToken(RefreshTokenReq request) {
        Claims  claims = jwtUtil.parseRefreshToken(request.refreshToken());
        Long userId = Long.parseLong(claims.getSubject());

        String savedRefreshToken = refreshService.getFromRedis(userId);
        savedRefreshToken = getOrLoadRefreshToken(userId, savedRefreshToken);
        invalidRefreshToken(request, savedRefreshToken);

        String accessToken = jwtUtil.createAccessToken(userId, UserRole.USER);
        String refreshToken = jwtUtil.createRefreshToken(userId);
        refreshService.update(userId, refreshToken);

        return TokenRes.of(accessToken, refreshToken);
    }

    @Transactional
    public void resetPassword(PasswordResetReq request) {
        emailService.checkEmailVerified(request.email());

        User user = userComponent.getByEmail(request.email());
        String encodedPassword = passwordEncoder.encode(request.password());
        user.resetPassword(encodedPassword);
    }

    // === Private Methods ===
    private void createOrUpdateRefreshToken(User savedUser, String refreshToken) {
        if (refreshService.existTokenAtDb(savedUser.getId())) {
            refreshService.update(savedUser.getId(), refreshToken);
        } else {
            refreshService.create(savedUser, refreshToken);
        }
    }

    private void validatePassword(LoginReq request, User savedUser) {
        if (!passwordEncoder.matches(request.password(), savedUser.getPassword())) {
            throw new InvalidException(ApiErrorCode.AUTH_INVALID_PASSWORD);
        }
    }

    private String getOrLoadRefreshToken(Long userId, String savedRefreshToken) {
        if (savedRefreshToken == null || savedRefreshToken.isBlank()) {
            RefreshToken token = refreshService.getFromDb(userId)
                    .orElseThrow(() -> new NotFoundException(ApiErrorCode.TOKEN_NOT_FOUND));
            return  token.getToken();
        }
        return savedRefreshToken;
    }

    private void invalidRefreshToken(RefreshTokenReq request, String savedRefreshToken) {
        if (!request.refreshToken().equals(savedRefreshToken)) {
            throw new UnAuthorizedException(ApiErrorCode.AUTH_UNAUTHORIZED_ACCESS);
        }
    }
}
