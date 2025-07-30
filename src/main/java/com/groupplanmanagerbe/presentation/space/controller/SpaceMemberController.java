package com.groupplanmanagerbe.presentation.space.controller;

import com.groupplanmanagerbe.domain.space.service.SpaceMemberService;
import com.groupplanmanagerbe.global.common.enums.ApiSuccessCode;
import com.groupplanmanagerbe.global.common.response.ApiSuccessRes;
import com.groupplanmanagerbe.global.security.model.AuthUser;
import com.groupplanmanagerbe.presentation.space.dto.response.InviteSpaceMemberRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/spaces")
@RequiredArgsConstructor
public class SpaceMemberController {

    private final SpaceMemberService spaceMemberService;

    @PostMapping("/{spaceId}/invitation")
    public ResponseEntity<ApiSuccessRes<InviteSpaceMemberRes>> inviteMember(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long spaceId
    ) {
        InviteSpaceMemberRes response = spaceMemberService.inviteMember(authUser.userId(), spaceId);
        return ApiSuccessRes.success(ApiSuccessCode.SUCCESS_SPACE_INVITE, response);
    }
}
