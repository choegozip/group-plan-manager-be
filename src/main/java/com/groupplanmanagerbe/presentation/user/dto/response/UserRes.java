package com.groupplanmanagerbe.presentation.user.dto.response;

import com.groupplanmanagerbe.domain.user.entity.User;
import lombok.Builder;

@Builder
public record UserRes(
        String nickname,
        String profileUrl
) {
    public static UserRes from(User user) {
        return UserRes.builder()
                .nickname(user.getNickname())
                .profileUrl(user.getProfileUrl())
                .build();
    }
}
