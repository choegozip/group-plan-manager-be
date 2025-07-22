package com.groupplanmanagerbe.global.security.handler;

import com.groupplanmanagerbe.global.common.enums.ApiErrorCode;
import com.groupplanmanagerbe.global.exception.FilterExceptionHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class GpmAuthEntryPoint implements AuthenticationEntryPoint {

    private final FilterExceptionHandler filterExceptionHandler;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {
        filterExceptionHandler.send(request, response, ApiErrorCode.AUTH_ACCESS_DENIED);
    }
}
