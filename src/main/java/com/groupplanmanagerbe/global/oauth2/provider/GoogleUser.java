package com.groupplanmanagerbe.global.oauth2.provider;

import com.groupplanmanagerbe.domain.social.entity.SocialUser;
import com.groupplanmanagerbe.global.common.enums.OAuthProvider;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Builder
public record GoogleUser(
        Map<String, Object> attributes,
        OAuthProvider provider,
        SocialUser socialUser
) implements ProviderUser {

    private static final String GOOGLE_ID_KEY = "sub";

    public static GoogleUser of(OAuth2User oAuth2User, OAuthProvider provider) {
        Map<String, Object> attrs = oAuth2User.getAttributes();
        return GoogleUser.builder()
                .attributes(attrs)
                .provider(provider)
                .build();
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get(GOOGLE_ID_KEY);
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getNickName() {
        return (String) attributes.get("name");
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public OAuthProvider getProvider() {
        return provider;
    }

    @Override
    public SocialUser getSocialUser() {
        return socialUser;
    }

    @Override
    public ProviderUser withSocialUser(SocialUser socialUser) {
        return GoogleUser.builder()
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
