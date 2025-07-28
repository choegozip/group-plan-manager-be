package com.groupplanmanagerbe.presentation.space.controller;

import com.groupplanmanagerbe.domain.space.service.SpaceService;
import com.groupplanmanagerbe.global.common.enums.ApiSuccessCode;
import com.groupplanmanagerbe.global.common.response.ApiSuccessRes;
import com.groupplanmanagerbe.global.security.model.AuthUser;
import com.groupplanmanagerbe.presentation.space.dto.request.CreateSpaceReq;
import io.swagger.v3.oas.models.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/spaces")
@RequiredArgsConstructor
public class SpaceController {

    private final SpaceService spaceService;

    @PostMapping
    public ResponseEntity<ApiSuccessRes<Void>> createSpace(
            @Valid @RequestBody CreateSpaceReq request,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        spaceService.createSpace(request, authUser.userId());
        return ApiSuccessRes.success(ApiSuccessCode.SUCCESS_CREATE_SPACE);
    }
}
