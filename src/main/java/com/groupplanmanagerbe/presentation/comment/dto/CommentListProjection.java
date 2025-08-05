package com.groupplanmanagerbe.presentation.comment.dto;

import java.time.LocalDateTime;

public interface CommentListProjection {
    Long getCommentId();
    String getContent();
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();

    Long getUserId();
    String getNickname();
    String getProfileImageKey();
}