package com.groupplanmanagerbe.domain.tobuycomment.repository;

import com.groupplanmanagerbe.domain.tobuycomment.entity.ToBuyComment;
import com.groupplanmanagerbe.presentation.comment.dto.CommentListProjection;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ToBuyCommentRepository extends JpaRepository<ToBuyComment, Long> {
    @EntityGraph(attributePaths = {"user"})
    List<ToBuyComment> findAllByToBuyItemId(Long toBuyItemId);

    @Query("SELECT c FROM ToBuyComment c " +
            "WHERE c.id = :commentId " +
            "AND c.user.id = :userId " +
            "AND c.user.deleted = false")
    Optional<ToBuyComment> findByIdAndUserId(@Param("userId") Long userId,
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
                FROM to_buy_comments c
                JOIN to_buy_items t ON c.to_buy_item_id = t.id
                JOIN users u ON c.user_id = u.id
                WHERE t.id = :toBuyId
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
            @Param("toBuyId") Long toBuyId,
            @Param("cursor") Long cursor,
            @Param("direction") String direction,
            @Param("size") int size
    );
}
