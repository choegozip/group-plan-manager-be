package com.groupplanmanagerbe.presentation.todoitem.dto.response;

import com.groupplanmanagerbe.domain.tobuycomment.entity.ToBuyComment;
import com.groupplanmanagerbe.domain.todocomment.entity.ToDoComment;
import com.groupplanmanagerbe.domain.todoitem.entity.ToDoItem;
import com.groupplanmanagerbe.domain.todoitem.entity.ToDoManager;
import com.groupplanmanagerbe.domain.user.entity.User;
import com.groupplanmanagerbe.global.common.enums.ManagerStatus;
import com.groupplanmanagerbe.global.common.enums.Urgency;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ToDoDetailRes(
        Long id,
        String title,
        String detail,
        LocalDateTime dueDate,
        Urgency urgency,
        String imageUrl,
        String referenceUrl,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        AuthorInfo author,
        List<ManagerInfo> managers,
        List<CommentInfo> comments
) {
    public static ToDoDetailRes of(ToDoItem item, List<ToDoComment> comments, List<ToDoManager> managers) {
        AuthorInfo author = AuthorInfo.of(item.getUser());
        List<ManagerInfo> managerInfosList = managers.stream()
                .map(ManagerInfo::of)
                .toList();
        List<CommentInfo> commentInfoList = comments.stream()
                .map(CommentInfo::of)
                .toList();

        return ToDoDetailRes.builder()
                .id(item.getId())
                .title(item.getTitle())
                .detail(item.getDetail())
                .dueDate(item.getDueDate())
                .urgency(item.getUrgency())
                .imageUrl(item.getImageUrl())
                .referenceUrl(item.getReferenceUrl())
                .createdAt(item.getCreatedAt())
                .updatedAt(item.getUpdatedAt())
                .author(author)
                .managers(managerInfosList)
                .comments(commentInfoList)
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
        public static ManagerInfo of(ToDoManager manager) {
            User user = manager.getUser();
            return ManagerInfo.builder()
                    .id(user.getId())
                    .nickname(user.getNickname())
                    .profileImageKey(user.getProfileImageKey())
                    .status(manager.getStatus())
                    .build();
        }
    }

    @Builder
    public record CommentInfo(
            Long id,
            String content,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            AuthorInfo author
    ) {
        public static CommentInfo of(ToDoComment comment) {
            return CommentInfo.builder()
                    .id(comment.getId())
                    .content(comment.getContent())
                    .createdAt(comment.getCreatedAt())
                    .updatedAt(comment.getUpdatedAt())
                    .author(AuthorInfo.of(comment.getUser()))
                    .build();

        }
    }
}
