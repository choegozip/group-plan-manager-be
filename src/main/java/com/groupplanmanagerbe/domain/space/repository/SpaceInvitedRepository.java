package com.groupplanmanagerbe.domain.space.repository;

import com.groupplanmanagerbe.domain.space.entity.SpaceInvited;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpaceInvitedRepository extends JpaRepository<SpaceInvited, Long> {
    Optional<SpaceInvited> findBySpaceId(Long spaceId);
}
