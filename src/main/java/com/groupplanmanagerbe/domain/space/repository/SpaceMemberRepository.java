package com.groupplanmanagerbe.domain.space.repository;

import com.groupplanmanagerbe.domain.space.entity.SpaceMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpaceMemberRepository extends JpaRepository<SpaceMember, Long> {
    List<SpaceMember> findAllBySpaceId(Long spaceId);
}
