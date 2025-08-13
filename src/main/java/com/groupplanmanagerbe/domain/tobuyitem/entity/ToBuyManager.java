package com.groupplanmanagerbe.domain.tobuyitem.entity;

import com.groupplanmanagerbe.domain.user.entity.User;
import com.groupplanmanagerbe.global.common.entity.BaseEntity;
import com.groupplanmanagerbe.global.common.enums.ManagerStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "to_buy_managers",
        indexes = @Index(name = "idx_manager_to_buy_user", columnList = "to_buy_item_id, user_id")
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ToBuyManager extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_buy_item_id")
    private ToBuyItem toBuyItem;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ManagerStatus status;

    @Builder
    public ToBuyManager(User user, ToBuyItem toBuyItem, ManagerStatus status) {
        this.user = user;
        this.toBuyItem = toBuyItem;
        this.status = status;
    }

    public static ToBuyManager of(User user, ToBuyItem toBuyItem) {
        ToBuyManager manager = ToBuyManager.builder()
                .user(user)
                .toBuyItem(toBuyItem)
                .status(ManagerStatus.PENDING)
                .build();
        toBuyItem.addManager(manager);
        return manager;
    }

    public void updateStatus(String status) {
        this.status = ManagerStatus.of(status);
    }
}
