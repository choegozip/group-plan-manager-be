package com.groupplanmanagerbe.domain.tobuyitem.entity;

import com.groupplanmanagerbe.domain.space.entity.Space;
import com.groupplanmanagerbe.domain.user.entity.User;
import com.groupplanmanagerbe.global.common.entity.BaseEntity;
import com.groupplanmanagerbe.global.common.enums.ManagerStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "to_buy_manager")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ToBuyManager extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_buy_item_id")
    private ToBuyItem toBuyItem;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ManagerStatus status;

    public ToBuyManager(User user, ToBuyItem toBuyItem, ManagerStatus managerStatus) {
        this.user = user;
        this.toBuyItem = toBuyItem;
        this.status = managerStatus;
    }
}
