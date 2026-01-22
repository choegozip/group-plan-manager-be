package com.groupplanmanagerbe.domain.user.service;

import com.groupplanmanagerbe.domain.user.entity.User;
import com.groupplanmanagerbe.domain.user.repository.UserRepository;
import com.groupplanmanagerbe.global.common.enums.ApiErrorCode;
import com.groupplanmanagerbe.global.exception.custom.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserComponent {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean isExist(String email) {
        return userRepository.existsByEmail(email);
    }

    public User getByIdAndDeleteFalse(Long userId) {
        return userRepository.findByIdAndDeletedFalse(userId)
                .orElseThrow(() -> new NotFoundException(ApiErrorCode.USER_NOT_FOUND));
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(ApiErrorCode.USER_NOT_FOUND));
    }

    public Optional<User> getOptionalByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
