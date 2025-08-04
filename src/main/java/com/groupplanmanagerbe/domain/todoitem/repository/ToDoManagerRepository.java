package com.groupplanmanagerbe.domain.todoitem.repository;

import com.groupplanmanagerbe.domain.todoitem.entity.ToDoManager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ToDoManagerRepository extends JpaRepository<ToDoManager, Long> {
}
