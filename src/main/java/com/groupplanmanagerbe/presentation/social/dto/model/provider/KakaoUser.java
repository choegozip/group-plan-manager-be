package com.groupplanmanagerbe.presentation.social.dto.model.provider;

import com.groupplanmanagerbe.global.common.enums.SocialProvider;
import lombok.Builder;

import java.util.Map;

@Builder
public record KakaoUser(
        Map<String, Object> attributes,
        SocialProvider provider
) implements ProviderUser {

    private static final String KAKAO_ID_KEY = "id";

    @SuppressWarnings("unchecked")
    public static KakaoUser of(Map<String, Object> response) {
        Map<String, Object> attributes = (Map<String, Object>) response.get("kakao_account");
        return KakaoUser.builder()
                .attributes(attributes)
                .provider(SocialProvider.KAKAO)
                .build();
    }

    @Override
    public String getProviderId() {
        return String.valueOf(attributes.get(KAKAO_ID_KEY));
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    @SuppressWarnings("unchecked")
    public String getNickName() {
        Map<String, Object> profile = (Map<String, Object>) attributes.get("profile");
        return (String) profile.get("nickname");
    }

    @Override
    public SocialProvider getProvider() {
        return provider;
    }
}
