package com.groupplanmanagerbe.domain.social.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.groupplanmanagerbe.domain.social.entity.SocialUser;
import com.groupplanmanagerbe.domain.social.repository.SocialUserRepository;
import com.groupplanmanagerbe.domain.user.entity.User;
import com.groupplanmanagerbe.domain.user.service.UserComponent;
import com.groupplanmanagerbe.global.common.enums.ApiErrorCode;
import com.groupplanmanagerbe.global.common.enums.SocialProvider;
import com.groupplanmanagerbe.global.exception.custom.InvalidException;
import com.groupplanmanagerbe.global.security.token.JwtUtil;
import com.groupplanmanagerbe.presentation.auth.dto.response.TokenRes;
import com.groupplanmanagerbe.presentation.social.dto.model.provider.GoogleUser;
import com.groupplanmanagerbe.presentation.social.dto.model.provider.KakaoUser;
import com.groupplanmanagerbe.presentation.social.dto.model.provider.NaverUser;
import com.groupplanmanagerbe.presentation.social.dto.model.provider.ProviderUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SocialUserService {

    private final SocialUserRepository socialUserRepository;
    private final UserComponent userComponent;
    private final RestTemplate restTemplate;
    private final JwtUtil jwtUtil;

    private static final String NAVER_USER_INFO_URL = "https://openapi.naver.com/v1/nid/me";
    private static final String KAKAO_USER_INFO_URL = "https://kauth.kakao.com/oauth/token";

    @Transactional
    public TokenRes socialLogin(String token, SocialProvider provider) throws FirebaseAuthException {
        ProviderUser providerUser;
        log.info("[SOCIAL_LOGIN] provider={} start", provider);
        switch (provider) {
            case GOOGLE -> {
                log.info("[Google] start");

                try {
                    var decodedToken =
                            FirebaseAuth.getInstance().verifyIdToken(token);

                    log.info("[Google] token verified");
                    providerUser = GoogleUser.of(FirebaseAuth.getInstance().verifyIdToken(token));
                    log.info("[GOOGLE] mapping done");
                } catch (FirebaseAuthException e) {
                    log.warn("[GOOGLE] verify fail", e);
                    throw e;
                }
            }
            case NAVER -> providerUser = NaverUser.of(getResponse(token, NAVER_USER_INFO_URL));
            case KAKAO -> providerUser = KakaoUser.of(getResponse(token, KAKAO_USER_INFO_URL));
            default -> throw new InvalidException(ApiErrorCode.PROVIDER_INVALID);
        }

        SocialUser socialUser = socialUserRepository.findByProviderAndProviderId(
                providerUser.getProvider(), providerUser.getProviderId())
                        .orElseGet(() -> createNewSocialUser(providerUser));

        return createTokenRes(socialUser.getUser());
    }

    // == private ==
    private Map<String, Object> getResponse(String accessToken, String infoUrl) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        log.info("헤더 받아옴");
        HttpEntity<Void> httpRequest = new HttpEntity<>(headers);
        return restTemplate.exchange(
                infoUrl,
                HttpMethod.GET,
                httpRequest,
                new ParameterizedTypeReference<Map<String, Object>>() {
                }
        ).getBody();
    }

    private SocialUser createNewSocialUser(ProviderUser provider) {
        log.info("신규 유저 등록점 진입");
        User user = findOrCreateUser(provider.getEmail(), provider.getNickName());
        log.info("유저 검색, 생성 성공");
        return socialUserRepository.save(SocialUser.of(user, provider.getProvider(), provider.getProviderId()));
    }

    private User findOrCreateUser(String email, String nickname) {
        return userComponent.getOptionalByEmail(email)
                .orElseGet(() -> userComponent
                        .saveUser(User.of(email, nickname, null)));
    }

    private TokenRes createTokenRes(User user) {
        log.info("토근 생성 진입 성공");
        String accessToken = jwtUtil.createAccessToken(user.getId(), user.getRole());
        String refreshToken = jwtUtil.createRefreshToken(user.getId());
        return TokenRes.of(accessToken, refreshToken);
    }
}
