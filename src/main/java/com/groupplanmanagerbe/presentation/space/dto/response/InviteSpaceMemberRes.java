package com.groupplanmanagerbe.presentation.space.dto.response;

import lombok.Builder;

@Builder
public record InviteSpaceMemberRes(
        String invitationUrl
) {
    public static InviteSpaceMemberRes of(String invitationUrl) {
        return InviteSpaceMemberRes.builder()
                .invitationUrl(invitationUrl)
                .build();
    }
}
