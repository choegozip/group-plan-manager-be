package com.groupplanmanagerbe.domain.auth.repository;

import com.groupplanmanagerbe.domain.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long>
{
}
