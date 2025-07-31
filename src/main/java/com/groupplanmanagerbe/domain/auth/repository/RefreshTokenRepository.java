package com.groupplanmanagerbe.domain.auth.repository;

import com.groupplanmanagerbe.domain.auth.entity.RefreshToken;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByUserId(Long userId);

    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.token = :token WHERE rt.user.id = :userId")
    void updateTokenByUserId(@Param("userId") Long userId, @Param("token") String token);
}
