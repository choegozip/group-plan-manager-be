package com.groupplanmanagerbe.domain.tobuyitem.repository;

import com.groupplanmanagerbe.domain.tobuyitem.entity.ToBuyItem;
import com.groupplanmanagerbe.presentation.tobuyitem.dto.ToBuyListProjection;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ToBuyItemRepository extends JpaRepository<ToBuyItem, Long> {

    @Query("SELECT COUNT(t) FROM ToBuyItem t " +
            "WHERE t.space.id = :spaceId")
    int countBySpaceId(@Param("spaceId") Long spaceId);

    @EntityGraph(attributePaths = {"user"})
    @Query("SELECT t FROM ToBuyItem t " +
            "WHERE t.id = :toBuyItemId " +
            "AND t.space.id = :spaceId " +
            "AND t.space.deleted = false")
    Optional<ToBuyItem> findByIdAndSpaceIdWithUser(@Param("toBuyItemId") Long toBuyItemId,
                                                    @Param("spaceId") Long spaceId);

    @Query("SELECT t FROM ToBuyItem t " +
            "WHERE t.id = :toBuyItemId " +
            "AND t.space.id = :spaceId " +
            "AND t.space.deleted = false " +
            "AND t.user.id = :userId " +
            "AND t.user.deleted = false")
    Optional<ToBuyItem> findByIdAndSpaceIdAndUserId(@Param("toBuyItemId") Long toBuyItemId,
                                                    @Param("spaceId") Long spaceId,
                                                    @Param("userId") Long userId);

    @EntityGraph(attributePaths = {"user", "space"})
    @Query("SELECT t FROM ToBuyItem t " +
            "WHERE t.id = :toBuyItemId " +
            "AND t.space.id = :spaceId " +
            "AND t.space.deleted = false " +
            "AND t.user.id = :userId " +
            "AND t.user.deleted = false")
    Optional<ToBuyItem> findByIdAndSpaceIdAndUserIdWithSpaceAndUser(@Param("toBuyItemId") Long toBuyItemId,
                                                                    @Param("spaceId") Long spaceId,
                                                                    @Param("userId") Long userId);

    @Query(value = """
            SELECT
                t.id AS to_buy_id,
                t.title,
                t.quantity,
                t.due_date,
                t.urgency,
                t.created_at,
                t.updated_at,
                u.id AS user_id,
                u.nickname,
                u.profile_image_key,
                (t.memo IS NOT NULL AND t.memo != '') AS hasMemo,
                (t.reference_url IS NOT NULL AND t.reference_url != '') AS hasLink,
                EXISTS (
                    SELECT 1 FROM to_buy_comments c WHERE c.to_buy_item_id = t.id
                ) AS hasComment
            FROM to_buy_items t
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
                SELECT 1 FROM to_buy_managers tm
                WHERE tm.to_buy_item_id = t.id AND tm.user_id = :managerId
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
    List<ToBuyListProjection> findToBuyItemsNative(
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
