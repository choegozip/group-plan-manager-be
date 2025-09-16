package com.groupplanmanagerbe.global.oauth2.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.groupplanmanagerbe.global.common.enums.ApiErrorCode;
import com.groupplanmanagerbe.global.common.response.ApiErrorRes;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class OAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception)
            throws IOException, ServletException {
        log.error("OAuth2 Authentication Failure");
        log.error("Request URL: {}", request.getRequestURL());
        log.error("Query String: {}", request.getQueryString());
        log.error("Full Error Trace: ", exception);
        
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        new ObjectMapper().writeValue(response.getWriter(),
                ApiErrorRes.of(ApiErrorCode.OAUTH2_LOGIN_FAILED.getCode(), exception.getMessage()));
    }
}
