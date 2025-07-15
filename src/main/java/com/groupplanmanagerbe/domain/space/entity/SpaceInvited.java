package com.groupplanmanagerbe.domain.space.entity;

import com.groupplanmanagerbe.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "space_invited")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SpaceInvited extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "space_id")
    private Space space;

    @Column(name = "invited_key", nullable = false)
    private String invitedKey;

    @Column(name = "invited_email")
    private String invitedToEmail;

    @Builder
    public SpaceInvited(Space space, String invitedKey, String invitedToEmail) {
        this.space = space;
        this.invitedKey = invitedKey;
        this.invitedToEmail = invitedToEmail;
    }
}
