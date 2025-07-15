package com.groupplanmanagerbe.domain.auth.service;

import com.groupplanmanagerbe.domain.auth.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlackListTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
}
