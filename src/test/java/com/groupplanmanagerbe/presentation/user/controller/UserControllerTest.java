package com.groupplanmanagerbe.presentation.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.groupplanmanagerbe.domain.user.service.UserService;
import com.groupplanmanagerbe.global.exception.GlobalExceptionHandler;
import com.groupplanmanagerbe.global.message.MessageResolver;
import com.groupplanmanagerbe.presentation.user.dto.request.CreateUserReq;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@Import(GlobalExceptionHandler.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private MessageResolver messageResolver;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
    private CreateUserReq validRequest;

    @BeforeEach
    void setup() {
        lenient().when(messageResolver.get(anyString()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler(messageResolver);

        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(exceptionHandler)
                .build();

        validRequest = new CreateUserReq(
                "test@example.com",
                "칙피",
                "Password123!",
                "https://image.url"
        );
    }

    @Test
    void 회원가입_성공() throws Exception {
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.code").value("success"));
    }

    @Test
    void 회원가입_유효하지_않은_값() throws Exception {
        CreateUserReq badReq = new CreateUserReq("333", "칙피", "Password123!", null);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badReq)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}