package com.groupplanmanagerbe.global.common.enums;

import com.groupplanmanagerbe.global.exception.custom.InvalidException;

public enum OAuthProvider {
    GOOGLE, NAVER, KAKAO;

    public static OAuthProvider of(String provider) {
        if (provider == null) return null;
        try {
            return OAuthProvider.valueOf(provider.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidException(ApiErrorCode.PROVIDER_INVALID);
        }
    }
}
