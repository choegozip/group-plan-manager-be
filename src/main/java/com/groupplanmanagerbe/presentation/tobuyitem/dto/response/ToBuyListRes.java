package com.groupplanmanagerbe.presentation.tobuyitem.dto.response;

import com.groupplanmanagerbe.domain.tobuyitem.entity.ToBuyItem;
import com.groupplanmanagerbe.domain.tobuyitem.entity.ToBuyManager;
import com.groupplanmanagerbe.domain.user.entity.User;
import com.groupplanmanagerbe.global.common.enums.ManagerStatus;
import com.groupplanmanagerbe.global.common.enums.Urgency;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ToBuyListRes(
        Long id,
        String title,
        Short quantity,
        LocalDateTime duDate,
        Urgency urgency,
        boolean hasMamo,
        boolean hasLink,
        boolean hasComment,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        AuthorInfo author,
        List<ManagerInfo> managers
) {
    public static ToBuyListRes of(ToBuyItem toBuyItem, List<ToBuyManager> managers) {
        AuthorInfo author = AuthorInfo.of(toBuyItem.getUser());
        List<ManagerInfo> managerInfos = managers.stream()
                .map(ManagerInfo::of)
                .toList();

        return ToBuyListRes.builder()
                .id(toBuyItem.getId())
                .title(toBuyItem.getTitle())
                .quantity(toBuyItem.getQuantity())
                .duDate(toBuyItem.getDueDate())
                .urgency(toBuyItem.getUrgency())
                .hasMamo(toBuyItem.getMemo() != null)
                .hasLink(toBuyItem.getReferenceUrl() != null)
                .hasComment(!toBuyItem.getToBuyComments().isEmpty())
                .createdAt(toBuyItem.getCreatedAt())
                .updatedAt(toBuyItem.getUpdatedAt())
                .author(author)
                .managers(managerInfos)
                .build();
    }

    @Builder
    public record AuthorInfo(
            Long id,
            String nickname,
            String profileImageKey
    ) {
        public static AuthorInfo of(User user) {
            return AuthorInfo.builder()
                    .id(user.getId())
                    .nickname(user.getNickname())
                    .profileImageKey(user.getProfileImageKey())
                    .build();
        }
    }

    @Builder
    public record ManagerInfo(
            Long id,
            String nickname,
            String profileImageKey,
            ManagerStatus status
    ) {
        public static ManagerInfo of(ToBuyManager manager) {
            return ManagerInfo.builder()
                    .id(manager.getUser().getId())
                    .nickname(manager.getUser().getNickname())
                    .profileImageKey(manager.getUser().getProfileImageKey())
                    .status(manager.getStatus())
                    .build();
        }
    }
}
