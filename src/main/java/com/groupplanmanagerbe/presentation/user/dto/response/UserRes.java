package com.groupplanmanagerbe.presentation.user.dto.response;

import com.groupplanmanagerbe.domain.user.entity.User;
import lombok.Builder;

@Builder
public record UserRes(
        Long id,
        String nickname,
        String profileImageKey
) {
    public static UserRes from(User user) {
        return UserRes.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .profileImageKey(user.getProfileImageKey())
                .build();
    }
}
