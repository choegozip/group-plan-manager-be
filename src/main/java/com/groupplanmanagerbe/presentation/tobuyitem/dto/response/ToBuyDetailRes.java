package com.groupplanmanagerbe.presentation.tobuyitem.dto.response;

import com.groupplanmanagerbe.domain.tobuycomment.entity.ToBuyComment;
import com.groupplanmanagerbe.domain.tobuyitem.entity.ToBuyItem;
import com.groupplanmanagerbe.domain.tobuyitem.entity.ToBuyManager;
import com.groupplanmanagerbe.domain.user.entity.User;
import com.groupplanmanagerbe.global.common.enums.ManagerStatus;
import com.groupplanmanagerbe.global.common.enums.Urgency;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ToBuyDetailRes(
        Long id,
        String title,
        Short quantity,
        LocalDateTime dueDate,
        Urgency urgency,
        String memo,
        String imageUrl,
        String referenceUrl,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        AuthorInfo author,
        List<ManagerInfo> managers,
        List<CommentInfo> comments
) {
    public static ToBuyDetailRes of(ToBuyItem item, List<ToBuyComment> comments, List<ToBuyManager> managers) {
        AuthorInfo author = AuthorInfo.of(item.getUser());
        List<ManagerInfo> managerInfosList = managers.stream()
                .map(ManagerInfo::of)
                .toList();
        List<CommentInfo> commentInfoList = comments.stream()
                .map(CommentInfo::of)
                .toList();

        return ToBuyDetailRes.builder()
                .id(item.getId())
                .title(item.getTitle())
                .quantity(item.getQuantity())
                .dueDate(item.getDueDate())
                .urgency(item.getUrgency())
                .memo(item.getMemo())
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
        public static ManagerInfo of(ToBuyManager manager) {
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
        public static CommentInfo of(ToBuyComment comment) {
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
