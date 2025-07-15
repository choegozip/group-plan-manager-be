package com.groupplanmanagerbe.domain.todoitem.service;

import com.groupplanmanagerbe.domain.todoitem.repository.ToDoItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ToDoItemService {
    private final ToDoItemRepository toDoItemRepository;
}
