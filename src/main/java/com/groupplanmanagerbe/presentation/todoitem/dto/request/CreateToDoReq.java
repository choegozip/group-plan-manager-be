package com.groupplanmanagerbe.presentation.todoitem.dto.request;

import com.groupplanmanagerbe.presentation.tobuyitem.dto.ItemValidationConst;
import jakarta.validation.constraints.*;

import java.util.List;

public record CreateToDoReq(
        @NotBlank(message = ItemValidationConst.TITLE_BLANK_MESSAGE)
        String title,

        String detail,

        @NotBlank(message = ItemValidationConst.DUE_DATE_INVALID_MESSAGE)
        @Pattern(regexp = ItemValidationConst.DUE_DATE_REG, message = ItemValidationConst.DUE_DATE_INVALID_MESSAGE)
        String dueDate,

        @NotNull(message = ItemValidationConst.URGENCY_BLANK_MESSAGE)
        String urgency,

        String imageUrl,
        String referenceUrl,

        @NotEmpty(message = ItemValidationConst.MANAGER_IDS_BLANK_MESSAGE)
        List<Long> managerIds
) {
}
