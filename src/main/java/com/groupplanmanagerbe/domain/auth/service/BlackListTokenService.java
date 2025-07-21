package com.groupplanmanagerbe.domain.auth.service;

import com.groupplanmanagerbe.global.security.model.JwtSecurityProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class BlackListTokenService {

    private final RedisTemplate<String, String> redisTemplate;
    private final JwtSecurityProperties jwtSecurityProperties;

    public void save(String token, long mills) {
        String key = getKey(token);
        redisTemplate.opsForValue().set(
                key, "blackListed", mills + TimeUnit.MINUTES.toMillis(1), TimeUnit.MILLISECONDS);
    }

    public boolean isBlacklisted(String token) {
        String key = getKey(token);
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    private String getKey(String token) {
        String prefix = jwtSecurityProperties.token().blackListPrefix();
        return prefix + token;
    }
}
