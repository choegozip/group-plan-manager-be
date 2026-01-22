package com.groupplanmanagerbe.presentation.social.dto.model.provider;

import com.groupplanmanagerbe.global.common.enums.SocialProvider;
import lombok.Builder;

import java.util.Map;

@Builder
public record NaverUser(
        Map<String, Object> attributes,
        SocialProvider provider
) implements ProviderUser {

    private static final String NAVER_ID_KEY = "id";

    @SuppressWarnings("unchecked")
    public static NaverUser of(Map<String, Object> response) {
        Map<String, Object> attributes = (Map<String, Object>) response.get("response");
        return NaverUser.builder()
                .attributes(attributes)
                .provider(SocialProvider.NAVER)
                .build();
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get(NAVER_ID_KEY);
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getNickName() {
        return (String) attributes.get("nickname");
    }

    @Override
    public SocialProvider getProvider() {
        return provider;
    }
}
