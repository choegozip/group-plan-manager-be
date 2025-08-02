package com.groupplanmanagerbe.domain.space.repository;

import com.groupplanmanagerbe.domain.space.entity.SpaceMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SpaceMemberRepository extends JpaRepository<SpaceMember, Long> {

    @Query("SELECT COUNT(sm) FROM SpaceMember sm " +
            "WHERE sm.user.id = :userId " +
            "AND sm.deleted = false")
    int countByUserId(Long userId);
}
