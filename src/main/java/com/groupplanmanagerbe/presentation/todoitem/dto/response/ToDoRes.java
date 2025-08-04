package com.groupplanmanagerbe.presentation.todoitem.dto.response;

import lombok.Builder;

@Builder
public record ToDoRes(
        Long id
) {
    public static ToDoRes of(Long id) {
        return ToDoRes.builder()
                .id(id)
                .build();
    }
}
