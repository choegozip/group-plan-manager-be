package com.groupplanmanagerbe.global.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.groupplanmanagerbe.global.common.enums.ApiErrorCode;
import com.groupplanmanagerbe.global.common.response.ApiErrorRes;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class FilterExceptionHandler {

    private final ObjectMapper objectMapper;
    private static final String contentType = "application/json";
    private final static String encodingFormat = "UTF-8";

    public void send(HttpServletResponse response, ApiErrorCode errorCode) throws IOException {
        send(response, ApiErrorRes.of(errorCode.getHttpStatus(), errorCode.getMessageKey()));
    }

    public void send(HttpServletResponse response, ApiErrorRes errorRes) throws IOException {
        response.setStatus(errorRes.httpStatus().value());
        response.setContentType(contentType);
        response.setCharacterEncoding(encodingFormat);

        objectMapper.writeValue(response.getOutputStream(), errorRes);
        response.getOutputStream().flush();
    }
}
