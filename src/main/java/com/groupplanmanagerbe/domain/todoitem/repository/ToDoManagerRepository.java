package com.groupplanmanagerbe.domain.todoitem.repository;

import com.groupplanmanagerbe.domain.tobuyitem.entity.ToBuyManager;
import com.groupplanmanagerbe.domain.todoitem.entity.ToDoManager;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ToDoManagerRepository extends JpaRepository<ToDoManager, Long> {
    @EntityGraph(attributePaths = {"toDoItem", "toDoItem.space"})
    @Query("SELECT tm FROM ToDoManager tm " +
            "WHERE tm.id = :managerId " +
            "AND tm.user.id = :userId " +
            "AND tm.user.deleted = false")
    Optional<ToDoManager> findByIdAndUserIdWithToDoAndSpace(@Param("managerId") Long managerId,
                                                              @Param("userId") Long userId);

    @EntityGraph(attributePaths = {"user"})
    @Query("""
        SELECT tm FROM ToDoManager tm
        JOIN FETCH tm.user
        WHERE tm.toDoItem.id IN :itemIds
        ORDER BY tm.toDoItem.id
        """)
    List<ToDoManager> findByToDoItemIdsWithUser(@Param("itemIds") List<Long> itemIds);
}
