package com.groupplanmanagerbe.presentation.space.dto.request;

import com.groupplanmanagerbe.presentation.space.dto.SpaceValidationConst;
import jakarta.validation.constraints.NotBlank;

public record CreateSpaceReq(

        @NotBlank(message = SpaceValidationConst.NAME_BLANK_MESSAGE)
        String name,

        @NotBlank(message = SpaceValidationConst.PROFILE_IMAGE_BLANK_MESSAGE)
        String profileImageKey
) {
}
