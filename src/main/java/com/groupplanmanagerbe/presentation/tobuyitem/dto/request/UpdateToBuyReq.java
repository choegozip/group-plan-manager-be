package com.groupplanmanagerbe.presentation.tobuyitem.dto.request;

import com.groupplanmanagerbe.presentation.tobuyitem.dto.ToBuyValidationConst;
import jakarta.validation.constraints.*;

import java.util.List;

public record UpdateToBuyReq(
        @NotBlank(message = ToBuyValidationConst.TITLE_BLANK_MESSAGE)
        String title,

        @Min(value = ToBuyValidationConst.QUANTITY_MIN, message = ToBuyValidationConst.QUANTITY_RANGE_MESSAGE)
        @Max(value = ToBuyValidationConst.QUANTITY_MAX, message = ToBuyValidationConst.QUANTITY_RANGE_MESSAGE)
        Short quantity,

        @Pattern(regexp = ToBuyValidationConst.DUE_DATE_REG, message = ToBuyValidationConst.DUE_DATE_INVALID_MESSAGE)
        String dueDate,

        String urgency,

        String memo,
        String imageUrl,
        String referenceUrl,

        List<Long> managerIds
) {
}
