package com.groupplanmanagerbe.domain.todoitem.repository;

import com.groupplanmanagerbe.domain.todoitem.entity.ToDoItem;
import com.groupplanmanagerbe.presentation.todoitem.dto.ToDoListProjection;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ToDoItemRepository extends JpaRepository<ToDoItem, Long> {

    @EntityGraph(attributePaths = {"user"})
    @Query("SELECT t FROM ToDoItem t " +
            "WHERE t.id = :toDoItemId " +
            "AND t.space.id = :spaceId " +
            "AND t.space.deleted = false")
    Optional<ToDoItem> findByIdAndSpaceIdWithUser(@Param("toDoItemId") Long toDoItemId,
                                                  @Param("spaceId") Long spaceId);

    @Query("SELECT t FROM ToDoItem t " +
            "WHERE t.id = :toDoItemId " +
            "AND t.space.id = :spaceId " +
            "AND t.space.deleted = false " +
            "AND t.user.id = :userId " +
            "AND t.user.deleted = false")
    Optional<ToDoItem> findByIdAndSpaceIdAndUserId(@Param("toDoItemId") Long toDoItemId,
                                                   @Param("spaceId") Long spaceId,
                                                   @Param("userId") Long userId);

    @EntityGraph(attributePaths = {"user", "space"})
    @Query("SELECT t FROM ToDoItem t " +
            "WHERE t.id = :toDoItemId " +
            "AND t.space.id = :spaceId " +
            "AND t.space.deleted = false " +
            "AND t.user.id = :userId " +
            "AND t.user.deleted = false")
    Optional<ToDoItem> findByIdAndSpaceIdAndUserIdWithSpaceAndUser(@Param("toDoItemId") Long toDoItemId,
                                                                   @Param("spaceId") Long spaceId,
                                                                   @Param("userId") Long userId);

    @Query(value = """
            SELECT
                t.id AS to_do_id,
                t.title,
                t.detail,
                t.due_date,
                t.urgency,
                t.created_at,
                t.updated_at,
                u.id AS user_id,
                u.nickname,
                (t.reference_url IS NOT NULL AND t.reference_url != '') AS hasLink,
                EXISTS (
                    SELECT 1 FROM to_buy_comments c WHERE c.to_buy_item_id = t.id
                ) AS hasComment
            FROM to_do_items t
            JOIN spaces s ON t.space_id = s.id
            JOIN users u ON t.user_id = u.id
            WHERE s.id = :spaceId
            AND s.deleted = false
            AND EXISTS (
                SELECT 1 FROM space_members sm
                JOIN users u ON sm.user_id = u.id
                WHERE sm.space_id = :spaceId
                AND sm.user_id = :userId
                AND u.deleted = false
            )
            AND (:managerId IS NULL OR EXISTS (
                SELECT 1 FROM to_do_managers tm
                WHERE tm.to_do_item_id = t.id AND tm.user_id = :managerId
            ))
            AND (:urgency IS NULL OR t.urgency = :urgency)
            AND (:includeExpired = TRUE OR t.due_date >= :now)
            AND (:cursor IS NULL OR (
                CASE WHEN :direction = 'DESC' THEN t.id < :cursor
                     ELSE t.id > :cursor END
            ))
            ORDER BY
                CASE WHEN :direction = 'DESC' THEN t.id END DESC,
                CASE WHEN :direction = 'ASC' THEN t.id END ASC
            LIMIT :size
            """, nativeQuery = true)
    List<ToDoListProjection> findToDoItemsNative(
            @Param("spaceId") Long spaceId,
            @Param("userId") Long userId,
            @Param("managerId") Long managerId,
            @Param("urgency") String urgency,
            @Param("cursor") Long cursor,
            @Param("direction") String direction,
            @Param("size") int size,
            @Param("includeExpired") boolean includeExpired,
            @Param("now") LocalDateTime now
    );
}
