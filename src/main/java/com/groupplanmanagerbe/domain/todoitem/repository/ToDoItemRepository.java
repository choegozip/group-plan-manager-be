package com.groupplanmanagerbe.domain.todoitem.repository;

import com.groupplanmanagerbe.domain.todoitem.entity.ToDoItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ToDoItemRepository extends JpaRepository<ToDoItem, Long> {
}
