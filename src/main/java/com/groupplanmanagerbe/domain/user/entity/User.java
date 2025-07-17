package com.groupplanmanagerbe.domain.user.entity;

import com.groupplanmanagerbe.domain.user.enums.UserRole;
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
    private UserRole role = UserRole.USER;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String profileUrl;

    private boolean isDelete;

    private LocalDateTime confirmedAt;

    @Builder
    public User(String nickname, String email, String password, String profileUrl) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.profileUrl = profileUrl;
    }

    public static User of(String email, String nickname, String password, String profileUrl) {
        return User.builder()
                .email(email)
                .nickname(nickname)
                .password(password)
                .profileUrl(profileUrl)
                .build();
    }
}
