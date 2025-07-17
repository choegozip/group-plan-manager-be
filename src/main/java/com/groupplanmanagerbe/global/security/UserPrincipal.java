package com.groupplanmanagerbe.global.security;

import com.groupplanmanagerbe.domain.user.enums.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

public record UserPrincipal(
        Long userId,
        Collection<? extends GrantedAuthority> authorities
) {

    public static UserPrincipal of(Long userId, UserRole role) {
        String prefixedRole = "ROLE_" + role.name();
        return new UserPrincipal(userId, List.of(new SimpleGrantedAuthority(prefixedRole)));
    }
}
