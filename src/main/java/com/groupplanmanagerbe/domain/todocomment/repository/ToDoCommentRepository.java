package com.groupplanmanagerbe.domain.todocomment.repository;

import com.groupplanmanagerbe.domain.todocomment.entity.ToDoComment;
import com.groupplanmanagerbe.presentation.comment.dto.CommentListProjection;
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

    @Query(value = """
                SELECT
                    c.id AS comment_id,
                    c.content,
                    c.created_at,
                    c.updated_at,
                    u.id AS user_id,
                    u.nickname,
                    u.profile_image_key
                FROM to_do_comments c
                JOIN to_do_items t ON c.to_do_item_id = t.id
                JOIN users u ON c.user_id = u.id
                    AND EXISTS (
                    SELECT 1 FROM space_members sm
                    JOIN users u ON sm.user_id = u.id
                    WHERE sm.space_id = :spaceId
                    AND sm.user_id = :userId
                    AND u.deleted = false
                )
                WHERE t.id = :toDoId
                  AND (
                        :cursor IS NULL
                        OR (:direction = 'DESC' AND c.id < :cursor)
                        OR (:direction = 'ASC' AND c.id > :cursor)
                    )
                ORDER BY
                  CASE WHEN :direction = 'DESC' THEN c.id END DESC,
                  CASE WHEN :direction = 'ASC' THEN c.id END ASC
                LIMIT :size
            """, nativeQuery = true)
    List<CommentListProjection> findCommentListNative(
            @Param("userId") Long userId,
            @Param("spaceId") Long spaceId,
            @Param("toDoId") Long toDoId,
            @Param("cursor") Long cursor,
            @Param("direction") String direction,
            @Param("size") int size
    );
}
