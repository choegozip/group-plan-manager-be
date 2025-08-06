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
import java.util.Optional;
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

        createToDb(user, refreshToken, getExpiryDate(expiration));
        saveToRedis(user.getId(), refreshToken, expiration);
    }

    public void update(Long userId, String refreshToken) {
        long expiration = jwtSecurityProperties.token().refreshExpiration();

        updateToDb(userId, refreshToken, getExpiryDate(expiration));
        saveToRedis(userId, refreshToken, expiration);
    }

    public void delete(Long userId) {
        deleteFromRedis(userId);
        deleteFromDb(userId);
    }

    public String getFromRedis(Long userId) {
        String key = getKey(userId);
        return redisTemplate.opsForValue().get(key);
    }

    public Optional<RefreshToken> getFromDb(Long userId) {
        return refreshRepository.findByUserId(userId);
    }

    public boolean existTokenAtDb(Long userId) {
        return refreshRepository.existsByUserId(userId);
    }

    // === Private Methods ===
    private void saveToRedis(Long userId, String refreshToken, long expiration) {
        String key = getKey(userId);
        redisTemplate.opsForValue().set(key, refreshToken, expiration, TimeUnit.MILLISECONDS);
    }

    private void createToDb(User user, String refreshToken, LocalDateTime expiryDate) {
        refreshRepository.save(RefreshToken.of(user, refreshToken, expiryDate));
    }

    private void updateToDb(Long userId, String refreshToken, LocalDateTime expiryDate) {
        refreshRepository.updateTokenByUserId(userId, refreshToken, expiryDate);
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

    private LocalDateTime getExpiryDate(long expiration) {
        return LocalDateTime.now().plus(Duration.ofMillis(expiration));
    }
}
