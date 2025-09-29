package com.groupplanmanagerbe.presentation.todoitem.dto.request;

import com.groupplanmanagerbe.presentation.tobuyitem.dto.ItemValidationConst;
import jakarta.validation.constraints.*;

import java.util.List;

public record UpdateToDoReq(
        String title,
        String detail,

        @Pattern(regexp = ItemValidationConst.DUE_DATE_REG, message = ItemValidationConst.DUE_DATE_INVALID_MESSAGE)
        String dueDate,

        String urgency,
        String imageUrl,
        String referenceUrl,

        @NotEmpty(message = ItemValidationConst.MANAGER_IDS_BLANK_MESSAGE)
        List<Long> managerIds
) {
}
