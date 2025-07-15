package com.groupplanmanagerbe.domain.tobuyitem.entity;

import com.groupplanmanagerbe.domain.space.entity.Space;
import com.groupplanmanagerbe.domain.user.entity.User;
import com.groupplanmanagerbe.global.common.entity.BaseEntity;
import com.groupplanmanagerbe.global.common.enums.Urgency;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "to_buy_item")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ToBuyItem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private short quantity;

    @Column(nullable = false, name = "due_date")
    private LocalDateTime dueDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Urgency urgency;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "reference_url")
    private String referenceUrl;

    private String memo;

    @Builder
    public ToBuyItem(
            User user, String title, short quantity, LocalDateTime dueDate, Urgency urgency,
            String imageUrl, String referenceUrl, String memo
    ) {
        this.user = user;
        this.title = title;
        this.quantity = quantity;
        this.dueDate = dueDate;
        this.urgency = urgency;
        this.imageUrl = imageUrl;
        this.referenceUrl = referenceUrl;
        this.memo = memo;
    }
}
