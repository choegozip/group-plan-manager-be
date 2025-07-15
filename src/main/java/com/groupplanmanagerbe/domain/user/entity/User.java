package com.groupplanmanagerbe.domain.user.entity;

import com.groupplanmanagerbe.domain.user.enums.Role;
import com.groupplanmanagerbe.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String profile_url;

    private boolean is_delete;

    private LocalDateTime confirmed_at;

    @Builder
    public User(String nickname, String email, String password, String profile_url) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.profile_url = profile_url;
    }
}
