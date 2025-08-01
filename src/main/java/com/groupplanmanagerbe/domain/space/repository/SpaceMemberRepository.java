package com.groupplanmanagerbe.domain.space.repository;

import com.groupplanmanagerbe.domain.space.entity.SpaceMember;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SpaceMemberRepository extends JpaRepository<SpaceMember, Long> {

    @Query("SELECT sm From SpaceMember sm WHERE sm.id IN :memberIds AND sm.space.id = :spaceId")
    List<SpaceMember> findAllByIdAndSpaceId(
            @Param("memberIds") List<Long> memberIds,
            @Param("spaceId") Long spaceId);

    @Query("SELECT COUNT(sm) FROM SpaceMember sm WHERE sm.user.id = :userId AND sm.deleted = false")
    int countByUserId(Long userId);
}
