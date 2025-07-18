package com.groupplanmanagerbe.global.security.model;

import com.groupplanmanagerbe.domain.user.enums.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

public record AuthUser(
        Long userId,
        Collection<? extends GrantedAuthority> authorities
) {

    public static AuthUser of(Long userId, UserRole role) {
        String prefixedRole = "ROLE_" + role.name();
        return new AuthUser(userId, List.of(new SimpleGrantedAuthority(prefixedRole)));
    }
}
