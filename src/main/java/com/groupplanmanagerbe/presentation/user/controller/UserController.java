package com.groupplanmanagerbe.presentation.user.controller;

import com.groupplanmanagerbe.domain.user.service.UserService;
import com.groupplanmanagerbe.global.common.enums.ApiSuccessCode;
import com.groupplanmanagerbe.global.common.response.ApiSuccessRes;
import com.groupplanmanagerbe.presentation.user.dto.request.CreateUserReq;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiSuccessRes<Void>> createUser(
            @Valid @RequestBody CreateUserReq request
    ) {
        userService.create(request);
        return ApiSuccessRes.created(ApiSuccessCode.SUCCESS_SIGNUP);
    }
}
