package com.groupplanmanagerbe.presentation.todoitem.dto;

import java.time.LocalDateTime;

public interface ToDoListProjection {
    Long getToDoId();
    String getTitle();
    String getDetail();
    LocalDateTime getDueDate();
    String getUrgency();
    boolean getHasLink();
    boolean getHasComment();
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();

    Long getUserId();
    String getNickname();
    String getProfileImageKey();
}
