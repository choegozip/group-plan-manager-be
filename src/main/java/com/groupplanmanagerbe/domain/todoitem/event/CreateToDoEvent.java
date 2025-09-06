package com.groupplanmanagerbe.domain.todoitem.event;

import com.groupplanmanagerbe.domain.todoitem.entity.ToDoManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class CreateToDoEvent {
    private final String itemType = "할 것";
    private final String author;
    private final String item;
    private final List<ToDoManager> managers;
}
