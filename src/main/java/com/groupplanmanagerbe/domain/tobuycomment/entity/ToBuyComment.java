package com.groupplanmanagerbe.domain.tobuycomment.entity;

import com.groupplanmanagerbe.domain.tobuyitem.entity.ToBuyItem;
import com.groupplanmanagerbe.domain.user.entity.User;
import com.groupplanmanagerbe.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "to_buy_comments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ToBuyComment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_buy_item_id")
    private ToBuyItem toBuyItem;

    @Column(nullable = false)
    private String content;

    @Builder
    public ToBuyComment(User user, ToBuyItem toBuyItem, String content) {
        this.user = user;
        this.toBuyItem = toBuyItem;
        this.content = content;
    }

    public static ToBuyComment of(ToBuyItem toBuyItem, User user, String content) {
        return ToBuyComment.builder()
                .user(user)
                .toBuyItem(toBuyItem)
                .content(content)
                .build();
    }
}
