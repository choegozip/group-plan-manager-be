package com.groupplanmanagerbe.global.security.token;

import com.groupplanmanagerbe.global.security.model.AuthUser;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final AuthUser authUser;
    private final String token;

    public JwtAuthenticationToken(AuthUser authUser, String token) {
        super(authUser.authorities());
        this.authUser = authUser;
        this.token = token;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return authUser;
    }
}
