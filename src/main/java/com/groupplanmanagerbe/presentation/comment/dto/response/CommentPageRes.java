package com.groupplanmanagerbe.presentation.comment.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record CommentPageRes(
        List<CommentListRes> list,
        boolean hasNext
) {
    public static CommentPageRes of(List<CommentListRes> list, int size) {
        return CommentPageRes.builder()
                .list(list)
                .hasNext(list.size() > size)
                .build();
    }
}
