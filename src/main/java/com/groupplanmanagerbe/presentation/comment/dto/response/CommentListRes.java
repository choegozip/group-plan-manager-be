package com.groupplanmanagerbe.presentation.comment.dto.response;

import com.groupplanmanagerbe.presentation.comment.dto.CommentListProjection;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CommentListRes(
        Long id,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        AuthorInfo author
) {
    public static CommentListRes from(CommentListProjection p) {
        AuthorInfo author = AuthorInfo.of(p);
        return CommentListRes.builder()
                .id(p.getCommentId())
                .content(p.getContent())
                .createdAt(p.getCreatedAt())
                .updatedAt(p.getUpdatedAt())
                .author(AuthorInfo.builder()
                        .id(p.getUserId())
                        .nickname(p.getNickname())
                        .profileImageKey(p.getProfileImageKey())
                        .build())
                .build();
    }

    @Builder
    public record AuthorInfo(
            Long id,
            String nickname,
            String profileImageKey
    ) {
        public static AuthorInfo of(CommentListProjection p) {
            return AuthorInfo.builder()
                    .id(p.getUserId())
                    .nickname(p.getNickname())
                    .profileImageKey(p.getProfileImageKey())
                    .build();
        }
    }
}
