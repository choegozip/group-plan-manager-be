package com.groupplanmanagerbe.presentation.tobuyitem.dto.response;

import com.groupplanmanagerbe.domain.tobuyitem.entity.ToBuyManager;
import com.groupplanmanagerbe.domain.user.entity.User;
import com.groupplanmanagerbe.global.common.enums.ManagerStatus;
import com.groupplanmanagerbe.global.common.enums.Urgency;
import com.groupplanmanagerbe.presentation.tobuyitem.dto.ToBuyListProjection;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ToBuyListRes(
        Long id,
        String title,
        Short quantity,
        LocalDateTime dueDate,
        Urgency urgency,
        boolean hasMemo,
        boolean hasLink,
        boolean hasComment,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        AuthorInfoForList author,
        List<ManagerInfoForList> managers
) {
    public static ToBuyListRes of(ToBuyListProjection projection, List<ToBuyManager> managers) {
        AuthorInfoForList author = AuthorInfoForList.of(projection);
        List<ManagerInfoForList> managerInfos = managers.stream()
                .map(ManagerInfoForList::of)
                .toList();

        return ToBuyListRes.builder()
                .id(projection.getToBuyId())
                .title(projection.getTitle())
                .quantity(projection.getQuantity())
                .dueDate(projection.getDueDate())
                .urgency(Urgency.of(projection.getUrgency()))
                .hasMemo(projection.getHasMemo())
                .hasLink(projection.getHasLink())
                .hasComment(projection.getHasComment())
                .createdAt(projection.getCreatedAt())
                .updatedAt(projection.getUpdatedAt())
                .author(author)
                .managers(managerInfos)
                .build();
    }

    @Builder
    public record AuthorInfoForList(
            Long id,
            String nickname,
            String profileImageKey
    ) {
        public static AuthorInfoForList of(ToBuyListProjection projection) {
            return AuthorInfoForList.builder()
                    .id(projection.getUserId())
                    .nickname(projection.getNickname())
                    .profileImageKey(projection.getProfileImageKey())
                    .build();
        }
    }

    @Builder
    public record ManagerInfoForList(
            Long id,
            String nickname,
            String profileImageKey,
            ManagerStatus status
    ) {
        public static ManagerInfoForList of(ToBuyManager manager) {
            User user = manager.getUser();
            return ManagerInfoForList.builder()
                    .id(user.getId())
                    .nickname(user.getNickname())
                    .profileImageKey(user.getProfileImageKey())
                    .status(manager.getStatus())
                    .build();
        }
    }
}
