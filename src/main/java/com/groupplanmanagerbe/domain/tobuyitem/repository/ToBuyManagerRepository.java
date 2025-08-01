package com.groupplanmanagerbe.domain.tobuyitem.repository;

import com.groupplanmanagerbe.domain.tobuyitem.entity.ToBuyManager;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ToBuyManagerRepository extends JpaRepository<ToBuyManager, Long> {
    @EntityGraph(attributePaths = {"toBuyItem", "toBuyItem.space"})
    @Query("SELECT tm FROM ToBuyManager tm " +
            "WHERE tm.id = :managerId " +
            "AND tm.user.id = :userId " +
            "AND tm.user.deleted = false")
    Optional<ToBuyManager> findByIdAndUserIdWithToBuyAndSpace(@Param("managerId") Long managerId,
                                                                  @Param("userId") Long userId);
}
