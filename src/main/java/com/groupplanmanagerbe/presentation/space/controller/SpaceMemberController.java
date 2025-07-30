package com.groupplanmanagerbe.presentation.space.controller;

import com.groupplanmanagerbe.domain.space.service.SpaceMemberService;
import com.groupplanmanagerbe.global.common.enums.ApiSuccessCode;
import com.groupplanmanagerbe.global.common.response.ApiSuccessRes;
import com.groupplanmanagerbe.global.security.model.AuthUser;
import com.groupplanmanagerbe.presentation.space.dto.request.JoinSpaceReq;
import com.groupplanmanagerbe.presentation.space.dto.response.spacemember.InviteSpaceMemberRes;
import com.groupplanmanagerbe.presentation.space.dto.response.spacemember.JoinSpaceRes;
import com.groupplanmanagerbe.presentation.space.dto.response.spacemember.SpaceMembersRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/join")
    public ResponseEntity<ApiSuccessRes<JoinSpaceRes>> joinSpace(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody JoinSpaceReq request
    ) {
        JoinSpaceRes response = spaceMemberService.joinSpace(authUser.userId(), request);
        return ApiSuccessRes.success(ApiSuccessCode.SUCCESS_JOIN_SPACE, response);
    }

    @DeleteMapping("/{spaceId}/members/{memberId}")
    public ResponseEntity<ApiSuccessRes<Void>> deleteMember(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long spaceId,
            @PathVariable Long memberId
    ) {
        spaceMemberService.deleteMember(authUser.userId(), spaceId, memberId);
        return ApiSuccessRes.success(ApiSuccessCode.SUCCESS_DELETE_SPACE_MEMBER);
    }

    @GetMapping("/{spaceId}/members")
    public ResponseEntity<ApiSuccessRes<List<SpaceMembersRes>>> getSpaceMembers(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long spaceId
    ) {
        List<SpaceMembersRes> response = spaceMemberService.getSpaceMember(authUser.userId(), spaceId);
        return ApiSuccessRes.success(ApiSuccessCode.SUCCESS_GET_SPACE_MEMBERS, response);
    }
}
