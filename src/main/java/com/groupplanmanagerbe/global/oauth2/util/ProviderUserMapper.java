package com.groupplanmanagerbe.global.oauth2.util;

import com.groupplanmanagerbe.global.common.enums.ApiErrorCode;
import com.groupplanmanagerbe.global.common.enums.SocialProvider;
import com.groupplanmanagerbe.global.exception.custom.InvalidException;
import com.groupplanmanagerbe.global.oauth2.provider.KakaoUser;
import com.groupplanmanagerbe.global.oauth2.provider.NaverUser;
import com.groupplanmanagerbe.global.oauth2.provider.ProviderUser;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class ProviderUserMapper {

    public ProviderUser toProviderUser(OAuth2User oAuth2User, OAuth2UserRequest userRequest) {
        SocialProvider provider = SocialProvider.of(userRequest.getClientRegistration().getRegistrationId());

        return switch (provider) {
            case NAVER -> NaverUser.of(oAuth2User, provider);
            case KAKAO -> KakaoUser.of(oAuth2User, provider);
            default -> throw new InvalidException(ApiErrorCode.PROVIDER_INVALID);
        };
    }
}
