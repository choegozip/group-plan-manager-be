package com.groupplanmanagerbe.domain.todoitem.repository;

import com.groupplanmanagerbe.domain.todoitem.entity.TodoManager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ToDoManagerRepository extends JpaRepository<TodoManager, Long> {
}
