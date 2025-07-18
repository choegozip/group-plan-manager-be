package com.groupplanmanagerbe.domain.auth.service;

import com.groupplanmanagerbe.domain.user.entity.User;
import com.groupplanmanagerbe.domain.user.repository.UserRepository;
import com.groupplanmanagerbe.domain.user.service.UserComponent;
import com.groupplanmanagerbe.global.common.enums.ApiErrorCode;
import com.groupplanmanagerbe.global.exception.custom.InvalidException;
import com.groupplanmanagerbe.global.security.token.JwtUtil;
import com.groupplanmanagerbe.presentation.auth.dto.request.LoginReq;
import com.groupplanmanagerbe.presentation.auth.dto.response.LoginRes;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final UserComponent userComponent;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public LoginRes login(LoginReq request) {
        User savedUser = userComponent.getByEmail(request.email());
        if (!passwordEncoder.matches(request.password(), savedUser.getPassword())) {
            throw new InvalidException(ApiErrorCode.AUTH_INVALID_PASSWORD);
        }
        String accessToken = jwtUtil.createAccessToken(savedUser.getId(), savedUser.getRole());
        String refreshToken = jwtUtil.createRefreshToken(savedUser.getId());
        return LoginRes.of(accessToken, refreshToken);
    }
}
