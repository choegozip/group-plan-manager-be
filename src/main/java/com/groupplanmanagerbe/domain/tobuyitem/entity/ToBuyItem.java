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
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "to_buy_items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ToBuyItem extends BaseEntity {
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

    @OneToMany(mappedBy = "toBuyItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ToBuyManager> managers = new ArrayList<>();

    @Builder
    public ToBuyItem(
            Space space, User user, String title, short quantity, LocalDateTime dueDate, Urgency urgency,
            String imageUrl, String referenceUrl, String memo
    ) {
        this.space = space;
        this.user = user;
        this.title = title;
        this.quantity = quantity;
        this.dueDate = dueDate;
        this.urgency = urgency;
        this.imageUrl = imageUrl;
        this.referenceUrl = referenceUrl;
        this.memo = memo;
    }

    public static ToBuyItem of(
            Space space, User user, String title, short quantity, LocalDateTime dueDate, String urgency,
            String imageUrl, String referenceUrl, String memo
    ) {
        return ToBuyItem.builder()
                .space(space)
                .user(user)
                .title(title)
                .quantity(quantity)
                .dueDate(dueDate)
                .urgency(Urgency.of(urgency))
                .imageUrl(imageUrl)
                .referenceUrl(referenceUrl)
                .memo(memo)
                .build();
    }

    public void addManager(ToBuyManager manager) {
        managers.add(manager);
        manager.setToBuyItem(this);
    }

    public void setManagers(List<ToBuyManager> managers) {
        this.managers.clear();

        for (ToBuyManager  manager : managers ) {
            addManager(manager);
        }
    }
}
