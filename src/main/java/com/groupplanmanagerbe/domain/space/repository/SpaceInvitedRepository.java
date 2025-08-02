package com.groupplanmanagerbe.domain.space.repository;

import com.groupplanmanagerbe.domain.space.entity.SpaceInvited;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SpaceInvitedRepository extends JpaRepository<SpaceInvited, String> {
    @Query("SELECT si FROM SpaceInvited si JOIN si.space s " +
            "WHERE s.id = :spaceId AND s.deleted = false")
    Optional<SpaceInvited> findBySpaceId(@Param("spaceId") Long spaceId);

    @EntityGraph(attributePaths = {"space", "space.members", "space.members.user"})
    @Query("SELECT si FROM SpaceInvited si JOIN si.space s " +
            "WHERE si.inviteKey = :inviteKey AND s.deleted = false")
    Optional<SpaceInvited> findByInviteKeyAndDeleted(@Param("inviteKey") String inviteKey);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @EntityGraph(attributePaths = {"space", "space.members", "space.members.user"})
    @Query("SELECT si FROM SpaceInvited si JOIN si.space s " +
            "WHERE si.inviteKey = :inviteKey AND s.deleted = false")
    Optional<SpaceInvited> findByInviteKeyAndDeletedWithLock(@Param("inviteKey") String inviteKey);
}
