package com.groupplanmanagerbe.presentation.social.dto.model.provider;

import com.groupplanmanagerbe.global.common.enums.SocialProvider;

public interface ProviderUser {
    String getProviderId();
    String getEmail();
    String getNickName();
    SocialProvider getProvider();
}
