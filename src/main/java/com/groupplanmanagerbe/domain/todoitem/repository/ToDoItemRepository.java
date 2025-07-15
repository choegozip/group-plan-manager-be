package com.groupplanmanagerbe.domain.todoitem.repository;

import com.groupplanmanagerbe.domain.todoitem.entity.ToDoItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ToDoItemRepository extends JpaRepository<ToDoItem, Long> {
}
