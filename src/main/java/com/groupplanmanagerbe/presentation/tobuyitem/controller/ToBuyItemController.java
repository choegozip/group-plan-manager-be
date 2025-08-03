package com.groupplanmanagerbe.presentation.tobuyitem.controller;

import com.groupplanmanagerbe.domain.tobuyitem.service.ToBuyItemService;
import com.groupplanmanagerbe.global.common.enums.ApiSuccessCode;
import com.groupplanmanagerbe.global.common.response.ApiSuccessRes;
import com.groupplanmanagerbe.global.common.response.page.CursorPageRequest;
import com.groupplanmanagerbe.global.security.model.AuthUser;
import com.groupplanmanagerbe.presentation.tobuyitem.dto.request.CreateToBuyReq;
import com.groupplanmanagerbe.presentation.tobuyitem.dto.request.UpdateManagerStatusReq;
import com.groupplanmanagerbe.presentation.tobuyitem.dto.request.UpdateToBuyReq;
import com.groupplanmanagerbe.presentation.tobuyitem.dto.response.ToBuyPageRes;
import com.groupplanmanagerbe.presentation.tobuyitem.dto.response.UpdateManagerStatusRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.SortDirection;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/spaces/{spaceId}/to-buy-items")
@RequiredArgsConstructor
public class ToBuyItemController {

    private final ToBuyItemService toBuyItemService;

    @PostMapping
    public ResponseEntity<ApiSuccessRes<Void>> createToBuy(
            @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody CreateToBuyReq request,
            @PathVariable Long spaceId
    ) {
        toBuyItemService.createToBuy(authUser.userId(), request, spaceId);
        return ApiSuccessRes.created(ApiSuccessCode.SUCCESS_TO_BUY_CREATE);
    }

    @PatchMapping("/{toBuyItemId}")
    public ResponseEntity<ApiSuccessRes<Void>> updateToBuy(
            @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody UpdateToBuyReq request,
            @PathVariable Long spaceId,
            @PathVariable Long toBuyItemId
    ) {
        toBuyItemService.updateToBuy(authUser.userId(), request, spaceId, toBuyItemId);
        return ApiSuccessRes.success(ApiSuccessCode.SUCCESS_TO_BUY_UPDATE);
    }

    @DeleteMapping("/{toBuyItemId}")
    public ResponseEntity<ApiSuccessRes<Void>> deleteToBuy(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long spaceId,
            @PathVariable Long toBuyItemId
    ) {
        toBuyItemService.deleteToBuy(authUser.userId(), spaceId, toBuyItemId);
        return ApiSuccessRes.success(ApiSuccessCode.SUCCESS_TO_BUY_DELETE);
    }

    @PatchMapping("/{toBuyItemId}/managers/{managerId}")
    public ResponseEntity<ApiSuccessRes<UpdateManagerStatusRes>> updateManagerStatus(
            @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody UpdateManagerStatusReq request,
            @PathVariable Long spaceId,
            @PathVariable Long toBuyItemId,
            @PathVariable Long managerId
    ) {
        UpdateManagerStatusRes response =
                toBuyItemService.updateManagerStatus(authUser.userId(), request, spaceId, toBuyItemId, managerId);
        return ApiSuccessRes.success(ApiSuccessCode.SUCCESS_UPDATE_MANAGER_STATUS, response);
    }

    @GetMapping
    public ResponseEntity<ApiSuccessRes<ToBuyPageRes>> getToBuyList(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long spaceId,
            @RequestParam int size,
            @RequestParam(defaultValue = "DESC") String direction,
            @RequestParam(required = false) Long cursor,
            @RequestParam(required = false) Long managerId,
            @RequestParam(required = false) String urgency
    ) {
        CursorPageRequest request = CursorPageRequest.of(cursor, size, direction, managerId, urgency);
        ToBuyPageRes response = toBuyItemService.getToBuyList(authUser.userId(), spaceId, request);
        return ApiSuccessRes.success(ApiSuccessCode.SUCCESS_GET_TO_BUY_LIST, response);
    }
}
