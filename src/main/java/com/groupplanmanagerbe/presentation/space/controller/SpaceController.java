package com.groupplanmanagerbe.presentation.space.controller;

import com.groupplanmanagerbe.domain.space.service.SpaceService;
import com.groupplanmanagerbe.global.common.enums.ApiSuccessCode;
import com.groupplanmanagerbe.global.common.response.ApiSuccessRes;
import com.groupplanmanagerbe.global.security.model.AuthUser;
import com.groupplanmanagerbe.presentation.space.dto.request.CreateSpaceReq;
import com.groupplanmanagerbe.presentation.space.dto.request.UpdateSpaceReq;
import com.groupplanmanagerbe.presentation.space.dto.response.space.SpaceCreateRes;
import com.groupplanmanagerbe.presentation.space.dto.response.space.SpaceRes;
import com.groupplanmanagerbe.presentation.space.dto.response.space.SpacesListRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/spaces")
@RequiredArgsConstructor
public class SpaceController {

    private final SpaceService spaceService;

    @PostMapping
    public ResponseEntity<ApiSuccessRes<SpaceCreateRes>> createSpace(
            @Valid @RequestBody CreateSpaceReq request,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        SpaceCreateRes response = spaceService.createSpace(request, authUser.userId());
        return ApiSuccessRes.created(ApiSuccessCode.SUCCESS_SPACE_CREATE, response);
    }

    @PatchMapping("/{spaceId}")
    public ResponseEntity<ApiSuccessRes<Void>> updateSpace(
            @PathVariable Long spaceId,
            @Valid @RequestBody UpdateSpaceReq request,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        spaceService.updateSpace(spaceId, request, authUser.userId());
        return ApiSuccessRes.success(ApiSuccessCode.SUCCESS_SPACE_UPDATE);
    }

    @DeleteMapping("/{spaceId}")
    public ResponseEntity<ApiSuccessRes<Void>> deleteSpace(
            @PathVariable Long spaceId,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        spaceService.deleteSpace(spaceId, authUser.userId());
        return ApiSuccessRes.success(ApiSuccessCode.SUCCESS_SPACE_DELETE);
    }

    @GetMapping
    public ResponseEntity<ApiSuccessRes<SpacesListRes>> getSpaces(
            @AuthenticationPrincipal AuthUser authUser
    ) {
        SpacesListRes response = spaceService.getSpaces(authUser.userId());
        return ApiSuccessRes.success(ApiSuccessCode.SUCCESS_SPACES_GET, response);
    }

    @GetMapping("/{spaceId}")
    public ResponseEntity<ApiSuccessRes<SpaceRes>> getSpace(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long spaceId
    ) {
        SpaceRes response = spaceService.getSpace(authUser.userId(), spaceId);
        return ApiSuccessRes.success(ApiSuccessCode.SUCCESS_SPACE_GET, response);
    }
}
