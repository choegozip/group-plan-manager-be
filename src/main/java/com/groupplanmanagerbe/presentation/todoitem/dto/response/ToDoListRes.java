package com.groupplanmanagerbe.presentation.todoitem.dto.response;

import com.groupplanmanagerbe.domain.todoitem.entity.ToDoItem;
import com.groupplanmanagerbe.domain.todoitem.entity.ToDoManager;
import com.groupplanmanagerbe.domain.user.entity.User;
import com.groupplanmanagerbe.global.common.enums.ManagerStatus;
import com.groupplanmanagerbe.global.common.enums.Urgency;
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
    public static ToDoListRes of(ToDoItem toDo, List<ToDoManager> managers) {
        AuthorInfoForList author = AuthorInfoForList.of(toDo.getUser());
        List<ManagerInfoForList> managerInfos = managers.stream()
                .map(ManagerInfoForList::of)
                .toList();
        return ToDoListRes.builder()
                .id(toDo.getId())
                .title(toDo.getTitle())
                .detail(toDo.getDetail())
                .duDate(toDo.getDueDate())
                .urgency(toDo.getUrgency())
                .hasLink(toDo.getReferenceUrl() != null)
                .hasComment(!toDo.getToDoComments().isEmpty())
                .createdAt(toDo.getCreatedAt())
                .updatedAt(toDo.getUpdatedAt())
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
        public static AuthorInfoForList of(User user) {
            return AuthorInfoForList.builder()
                    .id(user.getId())
                    .nickname(user.getNickname())
                    .profileImageKey(user.getProfileImageKey())
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
