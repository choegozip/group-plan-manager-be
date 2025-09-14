package com.groupplanmanagerbe.domain.user.entity;

import com.groupplanmanagerbe.domain.user.enums.UserRole;
import com.groupplanmanagerbe.global.common.entity.BaseEntity;
import com.groupplanmanagerbe.global.common.enums.ApiErrorCode;
import com.groupplanmanagerbe.global.exception.custom.InvalidException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "users",
        indexes = {
                @Index(name = "idx_user_email_deleted", columnList = "email, deleted", unique = true)
        }
)
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

    private String profileImageKey;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    private LocalDateTime confirmedAt;

    @Builder
    public User(String nickname, String email, String password, String profileImageKey) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.profileImageKey = profileImageKey;
    }

    public static User of(String email, String nickname, String password, String profileImageKey) {
        return User.builder()
                .email(email)
                .nickname(nickname)
                .password(password)
                .profileImageKey(profileImageKey)
                .build();
    }

    public void updateUserInfo(String nickname, String password, String profileImageKey) {
        if (nickname != null && !nickname.isBlank()) {
            updateNickname(nickname);
        }

        if (password != null && !password.isBlank()) {
            updatePassword(password);
        }

        if (profileImageKey != null && !profileImageKey.isBlank()) {
            updateProfileUrl(profileImageKey);
        }
    }

    private void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    private void updatePassword(String password) {
        this.password = password;
    }

    private void updateProfileUrl(String profileImageKey) {
        this.profileImageKey = profileImageKey;
    }

    public void delete() {
        if(this.deleted) {
            throw new InvalidException(ApiErrorCode.USER_ALREADY_DELETED);
        }
        this.deleted = true;
    }

    public void resetPassword(String encodedPassword) {
        this.password = encodedPassword;
    }
}
