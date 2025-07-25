package com.groupplanmanagerbe.domain.todoitem.entity;

import com.groupplanmanagerbe.domain.space.entity.Space;
import com.groupplanmanagerbe.domain.user.entity.User;
import com.groupplanmanagerbe.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "to_do_managers")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TodoManager extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "space_id")
    private Space space;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_do_item_id")
    private ToDoItem toDoItem;

    @Builder
    public TodoManager(User user, Space space, ToDoItem toDoItem) {
        this.user = user;
        this.space = space;
        this.toDoItem = toDoItem;
    }
}
