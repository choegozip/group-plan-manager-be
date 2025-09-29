package com.groupplanmanagerbe.global.common.enums;

import com.groupplanmanagerbe.global.exception.custom.InvalidException;

public enum SocialProvider {
    GOOGLE, NAVER, KAKAO;

    public static SocialProvider of(String provider) {
        if (provider == null) return null;
        try {
            return SocialProvider.valueOf(provider.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidException(ApiErrorCode.PROVIDER_INVALID);
        }
    }
}
