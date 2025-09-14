package com.groupplanmanagerbe.domain.tobuyitem.repository;

import com.groupplanmanagerbe.domain.tobuyitem.entity.ToBuyManager;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ToBuyManagerRepository extends JpaRepository<ToBuyManager, Long> {
    @EntityGraph(attributePaths = {"toBuyItem", "toBuyItem.user"})
    @Query("SELECT tm FROM ToBuyManager tm " +
            "WHERE tm.toBuyItem.id = :toBuyId " +
            "AND tm.toBuyItem.space.id = :spaceId " +
            "AND tm.toBuyItem.space.deleted = false " +
            "AND tm.user.id = :managerId " +
            "AND tm.user.deleted = false")
    Optional<ToBuyManager> findByIdAndSpaceIdAndToBuyIdWithToBuy(@Param("managerId") Long managerId,
                                                                 @Param("spaceId") Long spaceId,
                                                                 @Param("toBuyId") Long toBuyId);

    @EntityGraph(attributePaths = {"user"})
    @Query("""
            SELECT tm FROM ToBuyManager tm
            JOIN FETCH tm.user
            WHERE tm.toBuyItem.id IN :itemIds
            ORDER BY
                CASE WHEN tm.user.id = :userId THEN 0 ELSE 1 END,
                CASE tm.status
                    WHEN 'DONE' THEN 0
                    WHEN 'ACCEPT' THEN 1
                    ELSE 2
                END
            """)
    List<ToBuyManager> findByToBuyItemIdsWithUser(@Param("itemIds") List<Long> itemIds,
                                                  @Param("userId") Long userId);

    @EntityGraph(attributePaths = {"user"})
    @Query("""
            SELECT tm FROM ToBuyManager tm
            JOIN FETCH tm.user
            WHERE tm.toBuyItem.id = :toBuyItemId
               ORDER BY
                CASE WHEN tm.user.id = :userId THEN 0 ELSE 1 END,
                CASE tm.status
                    WHEN 'DONE' THEN 0
                    WHEN 'ACCEPT' THEN 1
                    ELSE 2
                END
            """)
    List<ToBuyManager> findAllByToBuyItemId(@Param("toBuyItemId") Long toBuyItemId,
                                            @Param("userId") Long userId);
}
