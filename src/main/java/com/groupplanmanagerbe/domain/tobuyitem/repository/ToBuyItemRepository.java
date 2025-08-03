package com.groupplanmanagerbe.domain.tobuyitem.repository;

import com.groupplanmanagerbe.domain.tobuyitem.entity.ToBuyItem;
import com.groupplanmanagerbe.global.common.enums.SortDirection;
import com.groupplanmanagerbe.global.common.enums.Urgency;
import com.groupplanmanagerbe.global.common.response.page.CursorPageRequest;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ToBuyItemRepository extends JpaRepository<ToBuyItem, Long> {

    @Query("SELECT t FROM ToBuyItem t " +
            "WHERE t.id = :toBuyItemId " +
            "AND t.user.id = :userId " +
            "AND t.user.deleted = false")
    Optional<ToBuyItem> findByIdAndUserId(@Param("toBuyItemId") Long toBuyItemId,
                                          @Param("userId") Long userId);

    @EntityGraph(attributePaths = {"space"})
    @Query("SELECT t FROM ToBuyItem t " +
            "WHERE t.id = :toBuyItemId " +
            "AND t.user.id = :userId " +
            "AND t.user.deleted = false")
    Optional<ToBuyItem> findByIdAndUserIdWithSpace(@Param("toBuyItemId") Long toBuyItemId,
                                                   @Param("userId") Long userId);


    @EntityGraph(attributePaths = {"user", "space"})
    @Query("SELECT t FROM ToBuyItem t " +
            "WHERE t.id = :toBuyItemId " +
            "AND t.user.id = :userId " +
            "AND t.user.deleted = false")
    Optional<ToBuyItem> findByIdAndUserIdWithSpaceAndUser(@Param("toBuyItemId") Long toBuyItemId,
                                                          @Param("userId") Long userId);

    @Query(value = """
        SELECT t.* FROM to_buy_items t
        JOIN spaces s ON t.space_id = s.id
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
        AND (:cursor IS NULL OR (
            CASE WHEN :direction = 'DESC' THEN t.id < :cursor
                 ELSE t.id > :cursor END
        ))
        ORDER BY
            CASE WHEN :direction = 'DESC' THEN t.id END DESC,
            CASE WHEN :direction = 'ASC' THEN t.id END ASC
        LIMIT :size
        """, nativeQuery = true)
    List<ToBuyItem> findToBuyItemsNative(
            @Param("spaceId") Long spaceId,
            @Param("userId") Long userId,
            @Param("managerId") Long managerId,
            @Param("urgency") String urgency,
            @Param("cursor") Long cursor,
            @Param("direction") String direction,
            @Param("size") int size
    );
}
