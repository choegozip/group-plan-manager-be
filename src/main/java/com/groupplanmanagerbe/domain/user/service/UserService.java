package com.groupplanmanagerbe.domain.user.service;

import com.groupplanmanagerbe.domain.user.entity.User;
import com.groupplanmanagerbe.domain.user.repository.UserRepository;
import com.groupplanmanagerbe.global.common.enums.ApiErrorCode;
import com.groupplanmanagerbe.global.exception.custom.DuplicateException;
import com.groupplanmanagerbe.presentation.user.dto.request.CreateUserReq;
import com.groupplanmanagerbe.presentation.user.dto.request.UpdateUserReq;
import com.groupplanmanagerbe.presentation.user.dto.response.UserRes;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserComponent userComponent;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void create(CreateUserReq request) {
        if (userComponent.isExist(request.email())) {
            throw new DuplicateException(ApiErrorCode.DUPLICATED_EMAIL);
        }

        String encodedPassword = passwordEncoder.encode(request.password());
        User newUser = User.of(request.email(), request.nickname(), encodedPassword, request.profileUrl());

        userRepository.save(newUser);
    }

    public UserRes get(Long userId) {
        User savedUser = userComponent.getById(userId);
        return UserRes.from(savedUser);
    }

    @Transactional
    public void update(Long userId, UpdateUserReq request) {
        User savedUser = userComponent.getById(userId);

        if (!(request.password() == null)) {
            String encodedPassword = passwordEncoder.encode(request.password());
            savedUser.updateUserInfo(request.nickname(), encodedPassword, request.profileUrl());
        }

        userRepository.save(savedUser);
    }
}
