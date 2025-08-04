package com.groupplanmanagerbe.domain.todocomment.repository;

import com.groupplanmanagerbe.domain.todocomment.entity.ToDoComment;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ToDoCommentRepository extends JpaRepository<ToDoComment, Long> {
    @EntityGraph(attributePaths = {"user"})
    List<ToDoComment> findAllByToDoItemId(Long toDoItemId);

    @Query("SELECT c FROM ToDoComment c " +
            "WHERE c.id = :commentId " +
            "AND c.user.id = :userId " +
            "AND c.user.deleted = false")
    Optional<ToDoComment> findByIdAndUserId(@Param("userId") Long userId,
                                             @Param("commentId") Long commentId);
}
