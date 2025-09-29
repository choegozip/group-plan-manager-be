package com.groupplanmanagerbe.global.oauth2.provider;

import com.groupplanmanagerbe.domain.social.entity.SocialUser;
import com.groupplanmanagerbe.global.common.enums.SocialProvider;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Builder
public record KakaoUser(
        Map<String, Object> attributes,
        SocialProvider provider,
        SocialUser socialUser
) implements ProviderUser {

    private static final String KAKAO_ID_KEY = "id";

    @SuppressWarnings("unchecked")
    public static KakaoUser of(OAuth2User oAuth2User, SocialProvider provider) {
        Map<String, Object> attrs = (Map<String, Object>) oAuth2User.getAttributes().get("kakao_account");
        return KakaoUser.builder()
                .attributes(attrs)
                .provider(provider)
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
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public SocialProvider getProvider() {
        return provider;
    }

    @Override
    public SocialUser getSocialUser() {
        return socialUser;
    }

    @Override
    public ProviderUser withSocialUser(SocialUser socialUser) {
        return KakaoUser.builder()
                .attributes(this.attributes)
                .provider(this.provider)
                .socialUser(socialUser)
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(socialUser.getUser().getRole().name()));
    }

    @Override
    public String getName() {
        return getProviderId();
    }
}
