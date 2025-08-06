package com.groupplanmanagerbe.domain.space.entity;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.groupplanmanagerbe.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "space_invited",
        indexes = @Index(name = "idx_space_invited_space_id", columnList = "space_id")
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SpaceInvited extends BaseEntity {
    @Id
    @Column(name = "invite_key", length = 21)
    private String inviteKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "space_id")
    private Space space;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Builder
    public SpaceInvited(Space space) {
        this.inviteKey = NanoIdUtils.randomNanoId(
                NanoIdUtils.DEFAULT_NUMBER_GENERATOR,
                NanoIdUtils.DEFAULT_ALPHABET,
                12);
        this.space = space;
        this.expiresAt = LocalDateTime.now().plusDays(3);
    }

    public static SpaceInvited of(Space space) {
        return SpaceInvited.builder()
                .space(space)
                .build();
    }
}
