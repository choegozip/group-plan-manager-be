package com.groupplanmanagerbe.presentation.tobuyitem.dto.request;

import com.groupplanmanagerbe.global.common.enums.Urgency;
import com.groupplanmanagerbe.presentation.tobuyitem.dto.ToBuyValidationConst;
import jakarta.validation.constraints.*;

import java.util.List;

public record CreateToBuyReq(
        @NotBlank(message = ToBuyValidationConst.TITLE_BLANK_MESSAGE)
        String title,

        @Min(value = ToBuyValidationConst.QUANTITY_MIN, message = ToBuyValidationConst.QUANTITY_RANGE_MESSAGE)
        @Max(value = ToBuyValidationConst.QUANTITY_MAX, message = ToBuyValidationConst.QUANTITY_RANGE_MESSAGE)
        short quantity,

        @NotBlank(message = ToBuyValidationConst.DUE_DATE_INVALID_MESSAGE)
        @Pattern(regexp = ToBuyValidationConst.DUE_DATE_REG, message = ToBuyValidationConst.DUE_DATE_INVALID_MESSAGE)
        String dueDate,

        @NotNull(message = ToBuyValidationConst.URGENCY_BLANK_MESSAGE)
        String urgency,

        String memo,
        String imageUrl,
        String referenceUrl,

        @NotEmpty(message = ToBuyValidationConst.MANAGER_IDS_BLANK_MESSAGE)
        List<Long> managerIds
) {
}