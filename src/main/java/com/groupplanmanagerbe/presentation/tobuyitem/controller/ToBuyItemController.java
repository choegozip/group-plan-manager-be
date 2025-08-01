package com.groupplanmanagerbe.presentation.tobuyitem.controller;

import com.groupplanmanagerbe.domain.tobuyitem.service.ToBuyItemService;
import com.groupplanmanagerbe.global.common.enums.ApiSuccessCode;
import com.groupplanmanagerbe.global.common.response.ApiSuccessRes;
import com.groupplanmanagerbe.global.security.model.AuthUser;
import com.groupplanmanagerbe.presentation.tobuyitem.dto.request.CreateToBuyReq;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
}
