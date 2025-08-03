package com.groupplanmanagerbe.presentation.tobuyitem.dto.response;

import com.groupplanmanagerbe.global.common.enums.ManagerStatus;
import lombok.Builder;

@Builder
public record UpdateManagerStatusRes(
        ManagerStatus status
) {
    public static UpdateManagerStatusRes of(ManagerStatus status) {
        return UpdateManagerStatusRes.builder()
                .status(status)
                .build();
    }
}
