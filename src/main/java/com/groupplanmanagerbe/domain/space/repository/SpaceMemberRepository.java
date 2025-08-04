package com.groupplanmanagerbe.domain.space.repository;

import com.groupplanmanagerbe.domain.space.entity.SpaceMember;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SpaceMemberRepository extends JpaRepository<SpaceMember, Long> {

    @Query("SELECT COUNT(sm) FROM SpaceMember sm " +
            "WHERE sm.user.id = :userId " +
            "AND sm.deleted = false")
    int countByUserId(Long userId);

    @Query("SELECT EXISTS (SELECT 1 FROM SpaceMember sm " +
            "WHERE sm.space.id = :spaceId " +
            "AND sm.user.id = :userId " +
            "AND sm.deleted = false)")
    boolean isSpaceMember(@Param("userId") Long userId,
                      @Param("spaceId") Long spaceId);
}
