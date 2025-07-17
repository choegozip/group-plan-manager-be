package com.groupplanmanagerbe.domain.user.service;

import com.groupplanmanagerbe.domain.user.entity.User;
import com.groupplanmanagerbe.domain.user.repository.UserRepository;
import com.groupplanmanagerbe.global.exception.custom.DuplicateException;
import com.groupplanmanagerbe.presentation.user.dto.request.CreateUserReq;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserComponent userComponent;

    @Mock
    private PasswordEncoder passwordEncoder;

    private CreateUserReq createUserReq;

    @BeforeEach
    void setup() {
        createUserReq = new CreateUserReq(
                "test@example.com",
                "칙피",
                "Password123!",
                "https://image.url"
        );
    }

    @Test
    void 회원가입_성공() {
        // given
        String encodedPassword = "a123b1234c324234d134235!";
        when(userComponent.isExist(createUserReq.email())).thenReturn(false);
        when(passwordEncoder.encode(createUserReq.password())).thenReturn(encodedPassword);

        // when
        userService.create(createUserReq);

        // then
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());

        User savedUser = captor.getValue();
        assertEquals(createUserReq.email(), savedUser.getEmail());
        assertEquals(createUserReq.nickname(), savedUser.getNickname());
        assertEquals(encodedPassword, savedUser.getPassword());
        assertEquals(createUserReq.profileUrl(), savedUser.getProfileUrl());
    }

    @Test
    void 회원가입_중복된_이메일() {
        // given
        when(userComponent.isExist(createUserReq.email())).thenReturn(true);

        // when & then
        assertThrows(DuplicateException.class, () -> {
            userService.create(createUserReq);
        });
    }
}