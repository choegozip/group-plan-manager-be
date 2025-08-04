package com.groupplanmanagerbe.presentation.comment.dto.request;

import com.groupplanmanagerbe.presentation.comment.dto.CommentValidation;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CreateCommentReq(
        @NotNull(message = CommentValidation.COMMENT_BLANK_MESSAGE)
        @Size(max = CommentValidation.COMMENT_MAX, message = CommentValidation.COMMENT_RANGE_MESSAGE)
        String content
) {
    public static CreateCommentReq of(String content) {
        return CreateCommentReq.builder()
                .content(content)
                .build();
    }
}

