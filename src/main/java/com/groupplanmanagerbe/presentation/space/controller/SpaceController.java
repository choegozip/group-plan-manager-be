package com.groupplanmanagerbe.presentation.space.controller;

import com.groupplanmanagerbe.domain.space.service.SpaceService;
import com.groupplanmanagerbe.global.common.enums.ApiSuccessCode;
import com.groupplanmanagerbe.global.common.response.ApiSuccessRes;
import com.groupplanmanagerbe.global.security.model.AuthUser;
import com.groupplanmanagerbe.presentation.space.dto.request.CreateSpaceReq;
import com.groupplanmanagerbe.global.common.response.page.CursorPageRequest;
import com.groupplanmanagerbe.presentation.space.dto.request.UpdateSpaceReq;
import com.groupplanmanagerbe.presentation.space.dto.response.SpacePageRes;
import com.groupplanmanagerbe.presentation.space.dto.response.SpacesRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.SortDirection;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
        return ApiSuccessRes.success(ApiSuccessCode.SUCCESS_SPACE_CREATE);
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
    public ResponseEntity<ApiSuccessRes<SpacePageRes>> getSpaces(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestParam(required = false) String cursor,
            @RequestParam int size,
            @RequestParam(defaultValue = "DESCENDING") SortDirection direction
    ) {
        CursorPageRequest request = CursorPageRequest.of(cursor, size, direction);

        SpacePageRes response = spaceService.getSpaces(request, authUser.userId());
        return ApiSuccessRes.success(ApiSuccessCode.SUCCESS_SPACES_GET, response);
    }
}
