package com.groupplanmanagerbe.presentation.comment.dto.response;

import lombok.Builder;

@Builder
public record CommentRes(
        Long id
) {
    public static CommentRes of(Long id) {
        return CommentRes.builder()
                .id(id)
                .build();
    }
}
