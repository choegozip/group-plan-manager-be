package com.groupplanmanagerbe.domain.todoitem.repository;

import com.groupplanmanagerbe.domain.todoitem.entity.ToDoManager;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ToDoManagerRepository extends JpaRepository<ToDoManager, Long> {
    @EntityGraph(attributePaths = {"toDoItem", "toDoItem.user"})
    @Query("SELECT tm FROM ToDoManager tm " +
            "WHERE tm.toDoItem.id = :toDoId " +
            "AND tm.toDoItem.space.id = :spaceId " +
            "AND tm.toDoItem.space.deleted = false " +
            "AND tm.user.id = :managerId " +
            "AND tm.user.deleted = false")
    Optional<ToDoManager> findByIdAndSpaceIdAndToDoIdWithToDo(@Param("managerId") Long managerId,
                                                              @Param("spaceId") Long spaceId,
                                                              @Param("toDoId") Long toDoId);

    @EntityGraph(attributePaths = {"user"})
    @Query("""
            SELECT tm FROM ToDoManager tm
            JOIN FETCH tm.user
            WHERE tm.toDoItem.id IN :itemIds
            ORDER BY
                CASE WHEN tm.user.id = :userId THEN 0 ELSE 1 END,
                CASE tm.status
                    WHEN 'DONE' THEN 0
                    WHEN 'ACCEPT' THEN 1
                    ELSE 2
                END
            """)
    List<ToDoManager> findByToDoItemIdsWithUser(@Param("itemIds") List<Long> itemIds,
                                                @Param("userId") Long userId);

    @EntityGraph(attributePaths = {"user"})
    @Query("""
            SELECT tm FROM ToDoManager tm
            JOIN FETCH tm.user
            WHERE tm.toDoItem.id = :toDoItemId
            ORDER BY
                CASE WHEN tm.user.id = :userId THEN 0 ELSE 1 END,
                CASE tm.status
                    WHEN 'DONE' THEN 0
                    WHEN 'ACCEPT' THEN 1
                    ELSE 2
                END
            """)
    List<ToDoManager> findAllByToDoItemId(@Param("toDoItemId") Long toDoItemId,
                                          @Param("userId") Long userId);
}
