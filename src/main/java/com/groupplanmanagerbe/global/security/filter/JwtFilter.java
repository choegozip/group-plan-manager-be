package com.groupplanmanagerbe.global.security.filter;

import com.groupplanmanagerbe.domain.auth.service.BlackListTokenService;
import com.groupplanmanagerbe.domain.user.enums.UserRole;
import com.groupplanmanagerbe.global.common.enums.ApiErrorCode;
import com.groupplanmanagerbe.global.exception.FilterExceptionHandler;
import com.groupplanmanagerbe.global.exception.custom.JwtTokenException;
import com.groupplanmanagerbe.global.security.model.JwtSecurityProperties;
import com.groupplanmanagerbe.global.security.model.AuthUser;
import com.groupplanmanagerbe.global.security.token.JwtAuthenticationToken;
import com.groupplanmanagerbe.global.security.token.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtSecurityProperties jwtSecurityProperties;
    private final BlackListTokenService blackListTokenService;
    private final JwtUtil jwtUtil;
    private final FilterExceptionHandler filterExceptionHandler;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return jwtSecurityProperties.secret().whiteList().stream()
                .anyMatch(whitelist -> antPathMatcher.match(whitelist, uri));
    }

    @Override
    public void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain
    ) throws IOException, ServletException {
        String bearerJwt = request.getHeader("Authorization");
        if (bearerJwt != null) {
            try {
            String token = jwtUtil.substringToken(bearerJwt);
            Claims claims = jwtUtil.validateToken(token, blackListTokenService);

                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    setAuthentication(token, claims);
                }
            } catch (JwtTokenException e) {
                log.warn("JWT 예외 발생: {}", e.getErrorCode());
                filterExceptionHandler.send(request, response, e.getErrorCode());
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private void setAuthentication(String token, Claims claims) {
        Long memberId = jwtUtil.getMemberIdFromToken(token, claims);
        UserRole role = jwtUtil.getRoleFromToken(token, claims);

        AuthUser authUser = AuthUser.of(memberId, role);
        JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(authUser, token);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}

