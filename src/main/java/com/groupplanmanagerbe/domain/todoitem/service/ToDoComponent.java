package com.groupplanmanagerbe.domain.todoitem.service;

import com.groupplanmanagerbe.domain.todoitem.entity.ToDoItem;
import com.groupplanmanagerbe.domain.todoitem.repository.ToDoItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ToDoComponent {

    private final ToDoItemRepository toDoItemRepository;

    public ToDoItem getReferenceById(Long toDoId) {
        return toDoItemRepository.getReferenceById(toDoId);
    }
}
