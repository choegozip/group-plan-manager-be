package com.groupplanmanagerbe.domain.todoitem.entity;

import com.groupplanmanagerbe.domain.user.entity.User;
import com.groupplanmanagerbe.global.common.entity.BaseEntity;
import com.groupplanmanagerbe.global.common.enums.ManagerStatus;
import com.groupplanmanagerbe.global.notification.listener.ItemManager;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "to_do_managers",
        indexes = @Index(name = "idx_manager_to_do_user", columnList = "to_do_item_id, user_id")
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ToDoManager extends BaseEntity implements ItemManager {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_do_item_id")
    private ToDoItem toDoItem;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ManagerStatus status;

    @Builder
    public ToDoManager(User user, ToDoItem toDoItem, ManagerStatus status) {
        this.user = user;
        this.toDoItem = toDoItem;
        this.status = status;
    }

    public static ToDoManager of(User user, ToDoItem toDoItem) {
        ToDoManager manager = ToDoManager.builder()
                .user(user)
                .toDoItem(toDoItem)
                .status(ManagerStatus.PENDING)
                .build();
        toDoItem.addManager(manager);
        return manager;
    }

    public void updateStatus(String status) {
        this.status = ManagerStatus.of(status);
    }
}
