package com.groupplanmanagerbe.presentation.comment.controller;

import com.groupplanmanagerbe.domain.todocomment.service.ToDoCommentService;
import com.groupplanmanagerbe.global.common.enums.ApiSuccessCode;
import com.groupplanmanagerbe.global.common.response.ApiSuccessRes;
import com.groupplanmanagerbe.global.security.model.AuthUser;
import com.groupplanmanagerbe.presentation.comment.dto.request.CommentReq;
import com.groupplanmanagerbe.presentation.comment.dto.response.CommentRes;
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
    public ResponseEntity<ApiSuccessRes<CommentRes>> createComment(
            @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody CommentReq request,
            @PathVariable Long spaceId,
            @PathVariable Long toDoItemId
    ) {
        CommentRes response = commentService.createComment(authUser.userId(), request, spaceId, toDoItemId);
        return ApiSuccessRes.success(ApiSuccessCode.SUCCESS_COMMENT_CREATE, response);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<ApiSuccessRes<CommentRes>> updateComment(
            @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody CommentReq request,
            @PathVariable Long toDoItemId,
            @PathVariable Long commentId
    ) {
        CommentRes response = commentService.updateComment(authUser.userId(), request, toDoItemId, commentId);
        return ApiSuccessRes.success(ApiSuccessCode.SUCCESS_COMMENT_UPDATE, response);
    }
}
