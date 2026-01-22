package com.groupplanmanagerbe.domain.user.service;

import com.groupplanmanagerbe.domain.mail.service.EmailService;
import com.groupplanmanagerbe.domain.user.entity.User;
import com.groupplanmanagerbe.domain.user.repository.UserRepository;
import com.groupplanmanagerbe.global.common.enums.ApiErrorCode;
import com.groupplanmanagerbe.global.exception.custom.DuplicateException;
import com.groupplanmanagerbe.presentation.user.dto.request.CreateUserReq;
import com.groupplanmanagerbe.presentation.user.dto.request.UpdateUserReq;
import com.groupplanmanagerbe.presentation.user.dto.response.UserCreateRes;
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
    private final EmailService emailService;

    @Transactional
    public UserCreateRes create(CreateUserReq request) {
        emailService.checkEmailVerified(request.email());

        if (userComponent.isExist(request.email())) {
            throw new DuplicateException(ApiErrorCode.USER_DUPLICATED_EMAIL);
        }

        String encodedPassword = passwordEncoder.encode(request.password());
        User newUser = User.of(request.email(), request.nickname(), encodedPassword, request.profileImageKey());

        userRepository.save(newUser);
        return UserCreateRes.of(newUser.getId());
    }

    public UserRes get(Long userId) {
        User savedUser = userComponent.getByIdAndDeleteFalse(userId);
        return UserRes.from(savedUser);
    }

    @Transactional
    public void update(Long userId, UpdateUserReq request) {
        User savedUser = userComponent.getByIdAndDeleteFalse(userId);

        String encodedPassword = null;
        if (request.password() != null && !request.password().isBlank()) {
            encodedPassword = passwordEncoder.encode(request.password());
        }

        savedUser.updateUserInfo(request.nickname(), encodedPassword, request.profileImageKey());
    }

    @Transactional
    public void delete(Long userId) {
        User savedUser = userComponent.getByIdAndDeleteFalse(userId);
        savedUser.delete();
    }
}
