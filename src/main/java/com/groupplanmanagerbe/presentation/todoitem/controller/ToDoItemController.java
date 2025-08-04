package com.groupplanmanagerbe.presentation.todoitem.controller;

import com.groupplanmanagerbe.domain.todoitem.service.ToDoItemService;
import com.groupplanmanagerbe.global.common.enums.ApiSuccessCode;
import com.groupplanmanagerbe.global.common.response.ApiSuccessRes;
import com.groupplanmanagerbe.global.security.model.AuthUser;
import com.groupplanmanagerbe.presentation.todoitem.dto.request.CreateToDoReq;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/spaces/{spaceId}/to-do-items")
@RequiredArgsConstructor
public class ToDoItemController {

    private final ToDoItemService toDoItemService;

    @PostMapping
    public ResponseEntity<ApiSuccessRes<Void>> createToDo(
            @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody CreateToDoReq request,
            @PathVariable Long spaceId
    ) {
        toDoItemService.createToDo(authUser.userId(), request, spaceId);
        return ApiSuccessRes.created(ApiSuccessCode.SUCCESS_TO_DO_CREATE);
    }
}
