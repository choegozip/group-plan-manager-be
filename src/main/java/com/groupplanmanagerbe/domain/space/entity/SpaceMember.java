package com.groupplanmanagerbe.domain.space.entity;

import com.groupplanmanagerbe.domain.user.entity.User;
import com.groupplanmanagerbe.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "space_member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SpaceMember extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "space_id")
    private Space space;

    @Column(name = "is_owner")
    private boolean isOwner = false;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    @Builder
    public SpaceMember(User user, Space space) {
        this.user = user;
        this.space = space;
    }

    public static SpaceMember of(User user, Space space) {
        SpaceMember member = SpaceMember.builder()
                .user(user)
                .space(space)
                .build();
        space.getMembers().add(member);
        return member;
    }

    public void makeOwner() {
        this.isOwner = true;
    }

    public void softDeleted() {
        this.deleted = true;
    }
}
