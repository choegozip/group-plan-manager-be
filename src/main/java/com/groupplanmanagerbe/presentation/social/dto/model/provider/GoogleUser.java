package com.groupplanmanagerbe.presentation.social.dto.model.provider;

import com.google.firebase.auth.FirebaseToken;
import com.groupplanmanagerbe.global.common.enums.SocialProvider;
import lombok.Builder;

@Builder
public record GoogleUser(
        String uid,
        String email,
        String nickname,
        SocialProvider provider
) implements ProviderUser {

    public static GoogleUser of(FirebaseToken token) {
        return GoogleUser.builder()
                .uid(token.getUid())
                .email(token.getEmail())
                .nickname(token.getName())
                .provider(SocialProvider.GOOGLE)
                .build();
    }

    @Override
    public String getProviderId() {
        return uid;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getNickName() {
        return nickname;
    }

    @Override
    public SocialProvider getProvider() {
        return provider;
    }
}
