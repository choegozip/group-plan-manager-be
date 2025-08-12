package com.groupplanmanagerbe.presentation.space.dto.response.spacemember;

import com.groupplanmanagerbe.domain.space.entity.Space;
import lombok.Builder;

@Builder
public record JoinSpaceRes(
        Long id,
        String nickname
) {
    public static JoinSpaceRes of(Space space) {
        return JoinSpaceRes.builder()
                .id(space.getId())
                .nickname(space.getName())
                .build();
    }
}
