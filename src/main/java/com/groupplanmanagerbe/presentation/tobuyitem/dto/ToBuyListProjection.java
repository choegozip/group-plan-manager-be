package com.groupplanmanagerbe.presentation.tobuyitem.dto;

import java.time.LocalDateTime;

public interface ToBuyListProjection {
    Long getToBuyId();
    String getTitle();
    Short getQuantity();
    LocalDateTime getDueDate();
    String getUrgency();
    boolean getHasMemo();
    boolean getHasLink();
    boolean getHasComment();
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();

    Long getUserId();
    String getNickname();
    String getProfileImageKey();
}