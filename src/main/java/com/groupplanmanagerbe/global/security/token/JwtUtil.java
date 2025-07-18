package com.groupplanmanagerbe.global.security.token;

import com.groupplanmanagerbe.domain.auth.service.BlackListService;
import com.groupplanmanagerbe.domain.user.enums.UserRole;
import com.groupplanmanagerbe.global.common.enums.ApiErrorCode;
import com.groupplanmanagerbe.global.exception.custom.JwtTokenException;
import com.groupplanmanagerbe.global.security.model.JwtSecurityProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtSecurityProperties securityProperties;
    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        String base64Secret = securityProperties.secret().key();
        if (!StringUtils.hasText(base64Secret)) {
            log.error("JWT secret key가 비어있습니다.");
            throw new JwtTokenException(ApiErrorCode.JWT_SECRET_KEY_EMPTY);
        }
        try {
            byte[] keyBytes = Base64.getDecoder().decode(base64Secret);
            this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        } catch (IllegalArgumentException e) {
            log.error("JWT secret key 디코딩 실패: {}", e.getMessage());
            throw new JwtTokenException(ApiErrorCode.JWT_SECRET_KEY_INVALID);
        }
    }

    public String createAccessToken(Long memberId, UserRole role) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + securityProperties.token().expiration());
        return Jwts.builder()
                .setSubject(memberId.toString())
                .claim("role", role.name())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(Long memberId) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + securityProperties.token().refreshExpiration());
        return Jwts.builder()
                .setSubject(memberId.toString())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String substringToken(String bearerToken) {
        String prefix = securityProperties.token().prefix();
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(prefix)) {
            return bearerToken.substring(prefix.length()).trim();
        }
        throw new JwtTokenException(ApiErrorCode.TOKEN_NOT_FOUND);
    }

    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.warn("토큰이 만료되었습니다: {}", e.getMessage());
            throw new JwtTokenException(ApiErrorCode.TOKEN_EXPIRED);
        } catch (UnsupportedJwtException e) {
            log.warn("지원하지 않는 토큰 형식입니다: {}", e.getMessage());
            throw new JwtTokenException(ApiErrorCode.TOKEN_UNSUPPORTED_FORMAT);
        } catch (MalformedJwtException e) {
            log.warn("잘못된 형식의 토큰입니다: {}", e.getMessage());
            throw new JwtTokenException(ApiErrorCode.TOKEN_MALFORMED);
        } catch (SecurityException e) {
            log.warn("토큰 서명이 유효하지 않습니다: {}", e.getMessage());
            throw new JwtTokenException(ApiErrorCode.TOKEN_INVALID_SIGNATURE);
        } catch (IllegalArgumentException e) {
            log.warn("토큰이 null이거나 빈 문자열입니다: {}", e.getMessage());
            throw new JwtTokenException(ApiErrorCode.TOKEN_EMPTY);
        }
    }

    /**
     * 토큰 유효성 검사 (블랙리스트 체크 있음)
     */
    public Claims validateToken(String token, BlackListService blacklistService) {
        // 1. 토큰 형식 및 서명 검증
        Claims claims = parseClaims(token);

        // 2. 블랙리스트 검증
        if (blacklistService != null && blacklistService.isBlacklisted(token)) {
            throw new JwtTokenException(ApiErrorCode.TOKEN_BLACKLISTED);
        }

        return claims;
    }

    /**
     * 토큰 유효성 검사 (블랙리스트 체크 없음)
     */
    public Claims validateToken(String token) {
        return validateToken(token, null);
    }

    /**
     * 토큰에서 회원 ID 조회
     */
    public Long getMemberIdFromToken(String token, Claims claims) {
        try {
            String subject = claims.getSubject();
            if (!StringUtils.hasText(subject)) {
                throw new JwtTokenException(ApiErrorCode.TOKEN_MISSING_MEMBER_ID);
            }
            return Long.parseLong(subject);
        } catch (NumberFormatException e) {
            log.warn("토큰의 subject가 유효한 회원 ID가 아닙니다: {}", e.getMessage());
            throw new JwtTokenException(ApiErrorCode.TOKEN_INVALID_MEMBER_ID);
        }
    }

    /**
     * 토큰에서 Role 조회
     */
    public UserRole getRoleFromToken(String token, Claims claims) {
        try {
            String roleStr = claims.get("role", String.class);
            if (!StringUtils.hasText(roleStr)) {
                throw new JwtTokenException(ApiErrorCode.TOKEN_MISSING_ROLE);
            }
            return UserRole.valueOf(roleStr);
        } catch (IllegalArgumentException e) {
            log.warn("토큰의 role이 유효하지 않습니다: {}", e.getMessage());
            throw new JwtTokenException(ApiErrorCode.TOKEN_INVALID_ROLE);
        }
    }

    /**
     * 토큰 발급 시간 조회
     */
    public Date getIssuedAtFromToken(String token) {
        Claims claims = parseClaims(token);
        return claims.getIssuedAt();
    }

    /**
     * 토큰 만료 시간 조회
     */
    public Date getExpirationFromToken(String token) {
        Claims claims = parseClaims(token);
        return claims.getExpiration();
    }
}