package com.groupplanmanagerbe.domain.todoitem.entity;

import com.groupplanmanagerbe.domain.space.entity.Space;
import com.groupplanmanagerbe.domain.todocomment.entity.ToDoComment;
import com.groupplanmanagerbe.domain.user.entity.User;
import com.groupplanmanagerbe.global.common.entity.BaseEntity;
import com.groupplanmanagerbe.global.common.enums.Urgency;
import com.groupplanmanagerbe.global.util.DateUtil;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "to_do_items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ToDoItem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "space_id", nullable = false)
    private Space space;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String title;

    private String detail;

    @Column(nullable = false, name = "due_date")
    private LocalDateTime dueDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Urgency urgency;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "reference_url")
    private String referenceUrl;

    @OneToMany(mappedBy = "toDoItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ToDoComment> toDoComments = new ArrayList<>();

    @OneToMany(mappedBy = "toDoItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ToDoManager> managers = new ArrayList<>();

    @Builder
    public ToDoItem(
            Space space, User user, String title, String detail, LocalDateTime dueDate,
            Urgency urgency, String imageUrl, String referenceUrl
    ) {
        this.space = space;
        this.user = user;
        this.title = title;
        this.detail = detail;
        this.dueDate = dueDate;
        this.urgency = urgency;
        this.imageUrl = imageUrl;
        this.referenceUrl = referenceUrl;
    }

    public static ToDoItem of(
            Space space, User user, String title, String detail, String dueDate,
            String urgency, String imageUrl, String referenceUrl
    ) {
        return ToDoItem.builder()
                .space(space)
                .user(user)
                .title(title)
                .detail(detail)
                .dueDate(DateUtil.isValidFutureDate(dueDate))
                .urgency(Urgency.of(urgency))
                .imageUrl(imageUrl)
                .referenceUrl(referenceUrl)
                .build();
    }

    public void addManager(ToDoManager manager) {
        managers.add(manager);
        manager.setToDoItem(this);
    }

    public void setManagers(List<ToDoManager> managers) {
        this.managers.clear();

        for (ToDoManager manager : managers ) {
            addManager(manager);
        }
    }

    public void updateToBuyItem(
            String title, String detail, String dueDate, String urgency,
            String imageUrl, String referenceUrl, List<ToDoManager> managers
    ) {
        if (title != null && !title.isBlank()) {
            this.title = title;
        }

        if (detail != null) {
            this.detail = detail;
        }

        if (dueDate != null) {
            this.dueDate = DateUtil.isValidFutureDate(dueDate);
        }

        if (urgency != null && !urgency.isBlank()) {
            this.urgency = Urgency.of(urgency);
        }

        if (imageUrl != null && !imageUrl.isBlank()) {
            this.imageUrl= imageUrl;
        }

        if (referenceUrl != null && !referenceUrl.isBlank()) {
            this.referenceUrl = referenceUrl;
        }

        if (!managers.isEmpty()) {
            setManagers(managers);
        }
    }
}
