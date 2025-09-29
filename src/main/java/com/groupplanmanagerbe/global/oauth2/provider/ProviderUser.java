package com.groupplanmanagerbe.global.oauth2.provider;

import com.groupplanmanagerbe.domain.social.entity.SocialUser;
import com.groupplanmanagerbe.global.common.enums.SocialProvider;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

public interface ProviderUser extends OAuth2User {
    String getProviderId();
    String getEmail();
    String getNickName();
    Map<String, Object> getAttributes();
    SocialProvider getProvider();
    SocialUser getSocialUser();
    ProviderUser withSocialUser(SocialUser socialUser);
}
