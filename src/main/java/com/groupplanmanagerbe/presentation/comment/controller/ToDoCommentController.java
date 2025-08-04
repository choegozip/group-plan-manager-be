package com.groupplanmanagerbe.presentation.comment.controller;

import com.groupplanmanagerbe.domain.todocomment.service.ToDoCommentService;
import com.groupplanmanagerbe.global.common.enums.ApiSuccessCode;
import com.groupplanmanagerbe.global.common.response.ApiSuccessRes;
import com.groupplanmanagerbe.global.security.model.AuthUser;
import com.groupplanmanagerbe.presentation.comment.dto.request.CreateCommentReq;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/spaces/{spaceId}/to-do-items/{toDoItemId}/comments")
@RequiredArgsConstructor
public class ToDoCommentController {

    private final ToDoCommentService  commentService;

    @PostMapping
    public ResponseEntity<ApiSuccessRes<Void>> createComment(
            @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody CreateCommentReq request,
            @PathVariable Long spaceId,
            @PathVariable Long toDoItemId
    ) {
        commentService.createComment(authUser.userId(), request, spaceId, toDoItemId);
        return ApiSuccessRes.success(ApiSuccessCode.SUCCESS_COMMENT_CREATE);
    }
}
