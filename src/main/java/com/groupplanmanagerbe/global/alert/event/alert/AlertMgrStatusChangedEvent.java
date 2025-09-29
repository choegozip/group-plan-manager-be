package com.groupplanmanagerbe.global.alert.event.alert;

import java.util.Locale;

public record AlertMgrStatusChangedEvent(
        Long authorId,
        String managerNickname,
        String item,
        String status,
        Locale locale
) {
}
