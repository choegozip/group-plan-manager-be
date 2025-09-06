package com.groupplanmanagerbe.domain.tobuyitem.event;

public record ChangeToBuyMgrStatusEvent(
        Long authorId,
        String managerNickname,
        String item,
        String status
) {
}
