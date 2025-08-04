package com.groupplanmanagerbe.presentation.todoitem.dto.request;

import com.groupplanmanagerbe.presentation.tobuyitem.dto.ItemValidationConst;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public record UpdateToDoReq(
        String title,
        String detail,

        @Pattern(regexp = ItemValidationConst.DUE_DATE_REG, message = ItemValidationConst.DUE_DATE_INVALID_MESSAGE)
        String dueDate,

        String urgency,
        String imageUrl,
        String referenceUrl,
        List<Long> managerIds
) {
}
