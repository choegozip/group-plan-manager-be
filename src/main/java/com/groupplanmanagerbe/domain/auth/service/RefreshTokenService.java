package com.groupplanmanagerbe.domain.auth.service;

import com.groupplanmanagerbe.domain.auth.entity.RefreshToken;
import com.groupplanmanagerbe.domain.auth.repository.RefreshTokenRepository;
import com.groupplanmanagerbe.domain.user.entity.User;
import com.groupplanmanagerbe.global.security.model.JwtSecurityProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final JwtSecurityProperties jwtSecurityProperties;

    public void create(User user, String refreshToken) {
        long expiration = jwtSecurityProperties.token().refreshExpiration();

        createToDb(user, refreshToken, expiration);
        createToRedis(user.getId(), refreshToken, expiration);
    }

    public void delete(Long userId) {
        deleteFromRedis(userId);
        deleteFromDb(userId);
    }

    private void createToDb(User user, String token, long expiration) {
        LocalDateTime expiryDate = LocalDateTime.now().plus(Duration.ofMillis(expiration));
        refreshRepository.save(RefreshToken.of(user, token, expiryDate));
    }

    private void createToRedis(Long userId, String token, long expiration) {
        String key = getKey(userId);
        redisTemplate.opsForValue().set(key, token, expiration, TimeUnit.MILLISECONDS);
    }

    private void deleteFromRedis(Long userId) {
        String key = getKey(userId);
        redisTemplate.delete(key);
    }

    private void deleteFromDb(Long userId) {
        if (refreshRepository.existsById(userId)) {
            refreshRepository.deleteById(userId);
        }
    }

    private String getKey(Long userId) {
        String prefix = jwtSecurityProperties.token().refreshPrefix();
        return prefix + userId;
    }
}
