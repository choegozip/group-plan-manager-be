package com.groupplanmanagerbe.domain.comment.entity;

import com.groupplanmanagerbe.domain.comment.enums.CommentParent;
import com.groupplanmanagerbe.domain.space.entity.Space;
import com.groupplanmanagerbe.domain.user.entity.User;
import com.groupplanmanagerbe.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "comments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "space_id")
    private Space space;

    @Column(name = "parent_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private CommentParent parent;

    @Column(name = "parent_id", nullable = false)
    private long parentId;

    @Column(nullable = false)
    private String comment;

    @Builder
    public Comment(User user, Space space, CommentParent parent, long parentId, String comment) {
        this.user = user;
        this.space = space;
        this.parent = parent;
        this.parentId = parentId;
        this.comment =comment;
    }
}
