package com.groupplanmanagerbe.global.alert.event.alert;

public record AlertMgrStatusChangedEvent(
        Long authorId,
        String managerNickname,
        String item,
        String status
) {
}
