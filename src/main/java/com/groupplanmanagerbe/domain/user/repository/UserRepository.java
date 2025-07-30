package com.groupplanmanagerbe.domain.user.repository;

import com.groupplanmanagerbe.domain.user.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT EXISTS(SELECT 1 FROM User u WHERE u.email = :email)")
    boolean existsByEmail(@Param("email") String email);

    Optional<User> findByEmail(String email);

    Optional<User> findByIdAndDeletedFalse(Long userId);
}
