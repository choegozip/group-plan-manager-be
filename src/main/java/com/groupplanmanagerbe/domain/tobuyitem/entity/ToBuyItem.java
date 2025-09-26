package com.groupplanmanagerbe.domain.tobuyitem.entity;

import com.groupplanmanagerbe.domain.tobuycomment.entity.ToBuyComment;
import com.groupplanmanagerbe.domain.space.entity.Space;
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
@Table(
        name = "to_buy_items",
        indexes = {
                @Index(name = "idx_to_buy_space_id", columnList = "space_id"),
                @Index(name = "idx_to_buy_user_id", columnList = "user_id")
        }
)
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
    private Short quantity;

    @Column(nullable = false, name = "due_date")
    private LocalDateTime dueDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Urgency urgency;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "reference_url")
    private String referenceUrl;

    private String memo;

    @OneToMany(mappedBy = "toBuyItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ToBuyComment> toBuyComments = new ArrayList<>();

    @OneToMany(mappedBy = "toBuyItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ToBuyManager> managers = new ArrayList<>();

    @Builder
    public ToBuyItem(
            Space space, User user, String title, Short quantity, LocalDateTime dueDate, Urgency urgency,
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
            Space space, User user, String title, Short quantity, String dueDate, String urgency,
            String imageUrl, String referenceUrl, String memo
    ) {
        return ToBuyItem.builder()
                .space(space)
                .user(user)
                .title(title)
                .quantity(quantity)
                .dueDate(DateUtil.isValidFutureDate(dueDate))
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

        for (ToBuyManager manager : managers) {
            addManager(manager);
        }
    }

    public void updateToBuyItem(
            String title, Short quantity, String dueDate, String urgency,
            String imageUrl, String referenceUrl, String memo, List<ToBuyManager> managers
    ) {
        if (title != null && !title.isBlank()) {
            this.title = title;
        }

        if (quantity != null) {
            this.quantity = quantity;
        }

        if (dueDate != null) {
            this.dueDate = DateUtil.isValidFutureDate(dueDate);
        }

        if (urgency != null && !urgency.isBlank()) {
            this.urgency = Urgency.of(urgency);
        }

        if (imageUrl != null && !imageUrl.isBlank()) {
            this.imageUrl = imageUrl;
        }

        if (referenceUrl != null && !referenceUrl.isBlank()) {
            this.referenceUrl = referenceUrl;
        }

        if (memo != null && !memo.isBlank()) {
            this.memo = memo;
        }

        if (managers != null) {
            setManagers(managers);
        }
    }

    public void addComment(ToBuyComment comment) {
        toBuyComments.add(comment);
        comment.setToBuyItem(this);
    }
}
