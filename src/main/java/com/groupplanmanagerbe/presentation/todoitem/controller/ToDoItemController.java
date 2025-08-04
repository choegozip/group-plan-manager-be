package com.groupplanmanagerbe.presentation.todoitem.controller;

import com.groupplanmanagerbe.domain.todoitem.service.ToDoItemService;
import com.groupplanmanagerbe.global.common.enums.ApiSuccessCode;
import com.groupplanmanagerbe.global.common.response.ApiSuccessRes;
import com.groupplanmanagerbe.global.security.model.AuthUser;
import com.groupplanmanagerbe.presentation.todoitem.dto.request.CreateToDoReq;
import com.groupplanmanagerbe.presentation.todoitem.dto.request.UpdateToDoReq;
import com.groupplanmanagerbe.presentation.todoitem.dto.response.ToDoRes;
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
    public ResponseEntity<ApiSuccessRes<ToDoRes>> createToDo(
            @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody CreateToDoReq request,
            @PathVariable Long spaceId
    ) {
        ToDoRes response = toDoItemService.createToDo(authUser.userId(), request, spaceId);
        return ApiSuccessRes.created(ApiSuccessCode.SUCCESS_TO_DO_CREATE, response);
    }

    @PatchMapping("/{toDoItemId}")
    public ResponseEntity<ApiSuccessRes<ToDoRes>> updateToDo(
            @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody UpdateToDoReq request,
            @PathVariable Long spaceId,
            @PathVariable Long toDoItemId
    ) {
        ToDoRes response = toDoItemService.updateToDo(authUser.userId(), request, spaceId, toDoItemId);
        return ApiSuccessRes.success(ApiSuccessCode.SUCCESS_TO_DO_UPDATE, response);
    }

    @DeleteMapping("/{toDoItemId}")
    public ResponseEntity<ApiSuccessRes<Void>> deleteToBuy(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long spaceId,
            @PathVariable Long toDoItemId
    ) {
        toDoItemService.deleteToDo(authUser.userId(), spaceId, toDoItemId);
        return ApiSuccessRes.success(ApiSuccessCode.SUCCESS_TO_DO_DELETE);
    }
}
