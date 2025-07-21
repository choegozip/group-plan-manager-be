package com.groupplanmanagerbe.global.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.groupplanmanagerbe.global.common.enums.ApiErrorCode;
import com.groupplanmanagerbe.global.common.response.ApiErrorRes;
import com.groupplanmanagerbe.global.message.MessageResolver;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class FilterExceptionHandler {

    private final ObjectMapper objectMapper;
    private final MessageResolver messageResolver;
    private static final String contentType = "application/json";
    private final static String encodingFormat = "UTF-8";

    public void send(HttpServletResponse response, ApiErrorCode errorCode) throws IOException {
        String localizedMessage = messageResolver.get(errorCode.getMessage());
        send(response, ApiErrorRes.of(errorCode.getCode(), localizedMessage), errorCode.getHttpStatus());
    }

    public void send(HttpServletResponse response, ApiErrorRes errorRes, HttpStatus httpStatus) throws IOException {
        response.setStatus(httpStatus.value());
        response.setContentType(contentType);
        response.setCharacterEncoding(encodingFormat);

        objectMapper.writeValue(response.getOutputStream(), errorRes);
        response.getOutputStream().flush();
    }
}
