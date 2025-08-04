package com.groupplanmanagerbe.presentation.tobuyitem.dto.request;

import com.groupplanmanagerbe.presentation.tobuyitem.dto.ItemValidationConst;
import jakarta.validation.constraints.NotNull;

public record UpdateManagerStatusReq(
        @NotNull(message = ItemValidationConst.MANAGER_STATUS_BLANK_MESSAGE)
        String managerStatus
) {
}
