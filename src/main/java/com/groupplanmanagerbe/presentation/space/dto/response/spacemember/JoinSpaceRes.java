package com.groupplanmanagerbe.presentation.space.dto.response.spacemember;

import com.groupplanmanagerbe.domain.space.entity.Space;
import lombok.Builder;

@Builder
public record JoinSpaceRes(
        Long spaceId,
        String spaceName,
        String landingUrl
) {
    public static JoinSpaceRes of(Space space, String landingUrl) {
        return JoinSpaceRes.builder()
                .spaceId(space.getId())
                .spaceName(space.getName())
                .landingUrl(landingUrl)
                .build();
    }
}
