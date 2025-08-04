package com.groupplanmanagerbe.domain.todoitem.repository;

import com.groupplanmanagerbe.domain.todoitem.entity.ToDoItem;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ToDoItemRepository extends JpaRepository<ToDoItem, Long> {
    @EntityGraph(attributePaths = {"user", "space"})
    @Query("SELECT t FROM ToDoItem t " +
            "WHERE t.id = :toDoItemId " +
            "AND t.user.id = :userId " +
            "AND t.user.deleted = false")
    Optional<ToDoItem> findByIdAndUserIdWithSpaceAndUser(@Param("toDoItemId") Long toDoItemId,
                                                          @Param("userId") Long userId);}
