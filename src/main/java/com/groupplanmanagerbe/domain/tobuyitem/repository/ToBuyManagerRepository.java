package com.groupplanmanagerbe.domain.tobuyitem.repository;

import com.groupplanmanagerbe.domain.tobuyitem.entity.ToBuyManager;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ToBuyManagerRepository extends JpaRepository<ToBuyManager, Long> {
    @EntityGraph(attributePaths = {"toBuyItem", "toBuyItem.space"})
    @Query("SELECT tm FROM ToBuyManager tm " +
            "WHERE tm.toBuyItem.id = :toBuyId " +
            "AND tm.user.id = :managerId " +
            "AND tm.user.deleted = false")
    Optional<ToBuyManager> findByIdAndToBuyIdWithToBuyAndSpace(@Param("managerId") Long managerId,
                                                               @Param("toBuyId") Long toBuyId);

    @EntityGraph(attributePaths = {"user"})
    @Query("""
        SELECT tm FROM ToBuyManager tm
        JOIN FETCH tm.user
        WHERE tm.toBuyItem.id IN :itemIds
        ORDER BY tm.toBuyItem.id
        """)
    List<ToBuyManager> findByToBuyItemIdsWithUser(@Param("itemIds") List<Long> itemIds);

    @EntityGraph(attributePaths = {"user"})
    List<ToBuyManager> findAllByToBuyItemId(Long toBuyItemId);
}
