package com.groupplanmanagerbe.global.message;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageResolver {

    private final MessageSource messageSource;

    public String get(String messageKey) {
        return messageSource.getMessage(messageKey, null, LocaleContextHolder.getLocale());
    }

    public String getFromFilter(String messageKey, HttpServletRequest request) {
        return messageSource.getMessage(messageKey, null, request.getLocale());
    }
}
