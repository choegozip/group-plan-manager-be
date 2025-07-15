package com.groupplanmanagerbe.domain.user.repository;

import com.groupplanmanagerbe.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
