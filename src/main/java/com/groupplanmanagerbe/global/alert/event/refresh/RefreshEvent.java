package com.groupplanmanagerbe.global.alert.event.refresh;

import com.groupplanmanagerbe.global.common.enums.RefreshType;

public record RefreshEvent(
        RefreshType refreshType,
        Long spaceId,
        Long actorId
) {
}
