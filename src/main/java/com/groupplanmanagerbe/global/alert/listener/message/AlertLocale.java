package com.groupplanmanagerbe.global.alert.listener.message;

import lombok.Builder;

@Builder
public record AlertLocale(
        String title,
        String body,
        String title_ko,
        String body_ko
) {
    public static AlertLocale of(String title, String body, String title_ko, String body_ko) {
        return AlertLocale.builder()
                .title(title)
                .body(body)
                .title_ko(title_ko)
                .body_ko(body_ko)
                .build();
    }
}
