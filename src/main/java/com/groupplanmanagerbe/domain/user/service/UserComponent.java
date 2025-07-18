package com.groupplanmanagerbe.domain.user.service;

import com.groupplanmanagerbe.domain.user.entity.User;
import com.groupplanmanagerbe.domain.user.repository.UserRepository;
import com.groupplanmanagerbe.global.common.enums.ApiErrorCode;
import com.groupplanmanagerbe.global.exception.custom.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserComponent {

    private final UserRepository userRepository;

    public boolean isExist(String email) {
        return userRepository.existsByEmail(email);
    }

    public User getById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ApiErrorCode.USER_NOT_FOUND));
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(ApiErrorCode.USER_NOT_FOUND));
    }
}
