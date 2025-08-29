package com.groupplanmanagerbe.presentation.tobuyitem.dto.request;

import com.groupplanmanagerbe.global.common.enums.Urgency;
import lombok.Builder;

@Builder
public record ParamReq(
        Long managerId,
        String urgency,
        boolean includeExpired
) {
    public static ParamReq of(Long managerId, String urgency, boolean includeExpired) {
        return ParamReq.builder()
                .managerId(managerId)
                .urgency(urgency != null ? Urgency.of(urgency).toString() : null)
                .includeExpired(includeExpired)
                .build();
    }
}
