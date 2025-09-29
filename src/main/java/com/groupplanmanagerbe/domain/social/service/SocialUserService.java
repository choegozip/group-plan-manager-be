package com.groupplanmanagerbe.domain.social.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.groupplanmanagerbe.domain.social.entity.SocialUser;
import com.groupplanmanagerbe.domain.social.repository.SocialUserRepository;
import com.groupplanmanagerbe.domain.user.entity.User;
import com.groupplanmanagerbe.domain.user.service.UserComponent;
import com.groupplanmanagerbe.global.common.enums.SocialProvider;
import com.groupplanmanagerbe.global.security.token.JwtUtil;
import com.groupplanmanagerbe.presentation.auth.dto.response.TokenRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SocialUserService {

    private final SocialUserRepository socialUserRepository;
    private final UserComponent userComponent;
    private final JwtUtil jwtUtil;

    @Transactional
    public TokenRes loginWithGoogle(String idToken) throws FirebaseAuthException {

        // 트라이 캐치 적용
        FirebaseToken verifiedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
        String uid = verifiedToken.getUid();
        String email = verifiedToken.getEmail();
        String nickname = verifiedToken.getName();

        SocialProvider provider = SocialProvider.GOOGLE;

        SocialUser socialUser = socialUserRepository.findByProviderAndProviderId(SocialProvider.GOOGLE, uid)
                .orElseGet(() -> createNewSocialUser(email, provider, uid, nickname));

        return createTokenRes(socialUser.getUser());
    }

    private SocialUser createNewSocialUser(String email, SocialProvider provider, String uid, String nickname) {
        User user = findOrCreateUser(email, nickname);
        return socialUserRepository.save(SocialUser.of(user, provider, uid));
    }

    private User findOrCreateUser(String email, String nickname) {
        return userComponent.getOptionalByEmail(email)
                .orElseGet(() -> userComponent
                        .saveUser(User.of(email, nickname,null)));
    }

    private TokenRes createTokenRes(User user) {
        String accessToken = jwtUtil.createAccessToken(user.getId(), user.getRole());
        String refreshToken = jwtUtil.createRefreshToken(user.getId());
        return TokenRes.of(accessToken, refreshToken);
    }
}
