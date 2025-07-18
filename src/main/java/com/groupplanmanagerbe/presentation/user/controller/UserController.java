package com.groupplanmanagerbe.presentation.user.controller;

import com.groupplanmanagerbe.domain.user.service.UserService;
import com.groupplanmanagerbe.global.common.enums.ApiSuccessCode;
import com.groupplanmanagerbe.global.common.response.ApiSuccessRes;
import com.groupplanmanagerbe.global.security.model.AuthUser;
import com.groupplanmanagerbe.presentation.user.dto.request.CreateUserReq;
import com.groupplanmanagerbe.presentation.user.dto.request.UpdateUserReq;
import com.groupplanmanagerbe.presentation.user.dto.response.UserRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @GetMapping("/me")
    public ResponseEntity<ApiSuccessRes<UserRes>> getUser(
            @AuthenticationPrincipal AuthUser authUser
    ) {
        UserRes response = userService.get(authUser.userId());
        return ApiSuccessRes.success(ApiSuccessCode.SUCCESS_USER_GET, response);
    }

    @PatchMapping("/me")
    public ResponseEntity<ApiSuccessRes<Void>> updateUser(
            @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody UpdateUserReq request
    ) {
        userService.update(authUser.userId(), request);
        return ApiSuccessRes.success(ApiSuccessCode.SUCCESS_USER_UPDATE);
    }

    @DeleteMapping("/me")
    public ResponseEntity<ApiSuccessRes<Void>> deleteUser(
            @AuthenticationPrincipal AuthUser authUser
    ) {
        userService.delete(authUser.userId());
        return ApiSuccessRes.success(ApiSuccessCode.SUCCESS_USER_DELETE);
    }
}
