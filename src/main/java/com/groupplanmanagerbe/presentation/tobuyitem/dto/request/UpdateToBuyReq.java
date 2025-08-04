package com.groupplanmanagerbe.presentation.tobuyitem.dto.request;

import com.groupplanmanagerbe.presentation.tobuyitem.dto.ItemValidationConst;
import jakarta.validation.constraints.*;

import java.util.List;

public record UpdateToBuyReq(
        @NotBlank(message = ItemValidationConst.TITLE_BLANK_MESSAGE)
        String title,

        @Min(value = ItemValidationConst.QUANTITY_MIN, message = ItemValidationConst.QUANTITY_RANGE_MESSAGE)
        @Max(value = ItemValidationConst.QUANTITY_MAX, message = ItemValidationConst.QUANTITY_RANGE_MESSAGE)
        Short quantity,

        @Pattern(regexp = ItemValidationConst.DUE_DATE_REG, message = ItemValidationConst.DUE_DATE_INVALID_MESSAGE)
        String dueDate,

        String urgency,

        String memo,
        String imageUrl,
        String referenceUrl,

        List<Long> managerIds
) {
}
