package com.groupplanmanagerbe.domain.todoitem.event;

public record ChangeToDoMgrStatusEvent(
        Long authorId,
        String managerNickname,
        String item,
        String status
) {
}
