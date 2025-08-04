package com.groupplanmanagerbe.presentation.todoitem.dto.response;

import lombok.Builder;
import java.util.List;

@Builder
public record ToDoPageRes(
        List<ToDoListRes> list,
        boolean hasNext
) {
    public static ToDoPageRes of(List<ToDoListRes> list, int size) {
        return ToDoPageRes.builder()
                .list(list)
                .hasNext(list.size() > size)
                .build();
    }
}
