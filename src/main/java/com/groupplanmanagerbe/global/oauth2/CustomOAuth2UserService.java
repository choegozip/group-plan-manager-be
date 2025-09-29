package com.groupplanmanagerbe.global.oauth2;

import com.groupplanmanagerbe.domain.social.entity.SocialUser;
import com.groupplanmanagerbe.domain.social.repository.SocialUserRepository;
import com.groupplanmanagerbe.domain.user.entity.User;
import com.groupplanmanagerbe.domain.user.service.UserComponent;
import com.groupplanmanagerbe.global.oauth2.provider.ProviderUser;
import com.groupplanmanagerbe.global.oauth2.util.ProviderUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final SocialUserRepository socialUserRepository;
    private final UserComponent userComponent;
    private final ProviderUserMapper providerUserMapper;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        ProviderUser providerUser = providerUserMapper.toProviderUser(oAuth2User, userRequest);

        SocialUser socialUser = ensureSocialUserExists(providerUser);

        return providerUser.withSocialUser(socialUser);
    }

    // ## private ##

    private SocialUser ensureSocialUserExists(ProviderUser providerUser) {
         return socialUserRepository.findByProviderAndProviderId(
                providerUser.getProvider(),
                providerUser.getProviderId()
        ).orElseGet(() -> createNewSocialUser(providerUser));
    }

    private SocialUser createNewSocialUser(ProviderUser providerUser) {
        User user = findOrCreateUser(providerUser);
        return socialUserRepository.save(SocialUser.of(user, providerUser.getProvider(), providerUser.getProviderId()));
    }

    private User findOrCreateUser(ProviderUser providerUser) {
        return userComponent.getOptionalByEmail(providerUser.getEmail())
                .orElseGet(() -> userComponent
                        .saveUser(User.of(providerUser.getEmail(), providerUser.getNickName(),null)));
    }
}
