package com.groupplanmanagerbe.domain.todoitem.entity;

import com.groupplanmanagerbe.domain.space.entity.Space;
import com.groupplanmanagerbe.domain.user.entity.User;
import com.groupplanmanagerbe.global.common.entity.BaseEntity;
import com.groupplanmanagerbe.global.common.enums.Urgency;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "to_do_items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ToDoItem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "space_id")
    private Space space;

    @Column(nullable = false)
    private String title;

    private String detail;

    @Column(nullable = false)
    private LocalDateTime dueDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Urgency urgency;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "reference_url")
    private String referenceUrl;

    public ToDoItem(
            User user, Space space, String title, String detail, LocalDateTime dueDate,
            Urgency urgency, String imageUrl, String referenceUrl
    ) {
        this.user = user;
        this.space = space;
        this.title = title;
        this.detail = detail;
        this.dueDate = dueDate;
        this.urgency = urgency;
        this.imageUrl = imageUrl;
        this.referenceUrl = referenceUrl;
    }
}
