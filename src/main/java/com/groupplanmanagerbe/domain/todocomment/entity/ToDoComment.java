package com.groupplanmanagerbe.domain.todocomment.entity;

import com.groupplanmanagerbe.domain.todoitem.entity.ToDoItem;
import com.groupplanmanagerbe.domain.user.entity.User;
import com.groupplanmanagerbe.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "to_do_comments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ToDoComment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_do_item_id")
    private ToDoItem toDoItem;

    @Column(nullable = false)
    private String content;

    @Builder
    public ToDoComment(User user, ToDoItem toDoItem, String content) {
        this.user = user;
        this.toDoItem = toDoItem;
        this.content = content;
    }

    public static ToDoComment of(ToDoItem toDo, User user, String content) {
        return ToDoComment.builder()
                .toDoItem(toDo)
                .user(user)
                .content(content)
                .build();
    }

    public void update(String content) {
        this.content = content;
    }
}
