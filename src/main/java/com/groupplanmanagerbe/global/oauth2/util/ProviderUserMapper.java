package com.groupplanmanagerbe.global.oauth2.util;

import com.groupplanmanagerbe.global.common.enums.OAuthProvider;
import com.groupplanmanagerbe.global.oauth2.provider.GoogleUser;
import com.groupplanmanagerbe.global.oauth2.provider.KakaoUser;
import com.groupplanmanagerbe.global.oauth2.provider.NaverUser;
import com.groupplanmanagerbe.global.oauth2.provider.ProviderUser;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class ProviderUserMapper {

    public ProviderUser toProviderUser(OAuth2User oAuth2User, OAuth2UserRequest userRequest) {
        OAuthProvider provider = OAuthProvider.of(userRequest.getClientRegistration().getRegistrationId());

        return switch (provider) {
            case GOOGLE -> GoogleUser.of(oAuth2User, provider);
            case NAVER -> NaverUser.of(oAuth2User, provider);
            case KAKAO -> KakaoUser.of(oAuth2User, provider);
        };
    }
}
