package com.groupplanmanagerbe.presentation.todoitem.dto.response;

import com.groupplanmanagerbe.domain.todoitem.entity.ToDoManager;
import com.groupplanmanagerbe.domain.user.entity.User;
import com.groupplanmanagerbe.global.common.enums.ManagerStatus;
import com.groupplanmanagerbe.global.common.enums.Urgency;
import com.groupplanmanagerbe.presentation.todoitem.dto.ToDoListProjection;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ToDoListRes(
        Long id,
        String title,
        String detail,
        LocalDateTime duDate,
        Urgency urgency,
        boolean hasLink,
        boolean hasComment,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        AuthorInfoForList author,
        List<ManagerInfoForList> managers
) {
    public static ToDoListRes of(ToDoListProjection projection, List<ToDoManager> managers) {
        AuthorInfoForList author = AuthorInfoForList.of(projection);
        List<ManagerInfoForList> managerInfos = managers.stream()
                .map(ManagerInfoForList::of)
                .toList();
        return ToDoListRes.builder()
                .id(projection.getToDoId())
                .title(projection.getTitle())
                .detail(projection.getDetail())
                .duDate(projection.getDueDate())
                .urgency(Urgency.of(projection.getUrgency()))
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
        public static AuthorInfoForList of(ToDoListProjection projection) {
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
        public static ManagerInfoForList of(ToDoManager manager) {
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
