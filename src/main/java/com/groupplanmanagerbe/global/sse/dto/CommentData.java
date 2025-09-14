package com.groupplanmanagerbe.global.sse.dto;

import lombok.Builder;

@Builder
public record CommentData(
        Long id,
        boolean hasComment
) {
    public static CommentData of(Long id){
        return CommentData.builder()
                .id(id)
                .hasComment(true)
                .build();

    }
}
