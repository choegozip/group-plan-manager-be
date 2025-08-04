package com.groupplanmanagerbe.domain.todocomment.repository;

import com.groupplanmanagerbe.domain.todocomment.entity.ToDoComment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ToDoCommentRepository extends JpaRepository<ToDoComment, Long> {
    @EntityGraph(attributePaths = {"user"})
    List<ToDoComment> findAllByToDoItemId(Long toDoItemId);
}
