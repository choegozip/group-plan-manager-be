package com.groupplanmanagerbe.global.security.handler;

import com.groupplanmanagerbe.global.common.enums.ApiErrorCode;
import com.groupplanmanagerbe.global.exception.FilterExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class GpmAccessDeniedHandler implements AccessDeniedHandler {

    private final FilterExceptionHandler filterExceptionHandler;

    // 인증은 됐으나, 유저롤이 달라서 인가가 실패한 경우
    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException {
        filterExceptionHandler.send(request, response, ApiErrorCode.ACCESS_DENIED);
    }
}
