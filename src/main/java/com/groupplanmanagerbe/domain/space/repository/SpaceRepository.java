package com.groupplanmanagerbe.domain.space.repository;

import com.groupplanmanagerbe.domain.space.entity.Space;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SpaceRepository extends JpaRepository<Space, Long> {
   @Query("SELECT s From Space s JOIN s.members m " +
           "WHERE s.id = :spaceId AND s.deleted = false " +
           "AND m.user.id = :userId AND m.user.deleted = false")
   Optional<Space> findByIdAndUserId(@Param("spaceId")Long spaceId, @Param("userId")Long userId);
}
