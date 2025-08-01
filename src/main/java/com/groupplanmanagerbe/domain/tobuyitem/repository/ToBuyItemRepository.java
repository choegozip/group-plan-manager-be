package com.groupplanmanagerbe.domain.tobuyitem.repository;

import com.groupplanmanagerbe.domain.tobuyitem.entity.ToBuyItem;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
}
