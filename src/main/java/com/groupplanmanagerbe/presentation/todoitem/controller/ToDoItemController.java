package com.groupplanmanagerbe.presentation.todoitem.controller;

import com.groupplanmanagerbe.domain.todoitem.service.ToDoItemService;
import com.groupplanmanagerbe.global.common.enums.ApiSuccessCode;
import com.groupplanmanagerbe.global.common.response.ApiSuccessRes;
import com.groupplanmanagerbe.global.common.response.page.CursorPageRequest;
import com.groupplanmanagerbe.global.security.model.AuthUser;
import com.groupplanmanagerbe.presentation.tobuyitem.dto.request.ParamReq;
import com.groupplanmanagerbe.presentation.tobuyitem.dto.request.UpdateManagerStatusReq;
import com.groupplanmanagerbe.presentation.tobuyitem.dto.response.UpdateManagerStatusRes;
import com.groupplanmanagerbe.presentation.todoitem.dto.request.CreateToDoReq;
import com.groupplanmanagerbe.presentation.todoitem.dto.request.UpdateToDoReq;
import com.groupplanmanagerbe.presentation.todoitem.dto.response.ToDoDetailRes;
import com.groupplanmanagerbe.presentation.todoitem.dto.response.ToDoPageRes;
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

    @PatchMapping("/{toDoItemId}/managers/{userId}")
    public ResponseEntity<ApiSuccessRes<UpdateManagerStatusRes>> updateManagerStatus(
            @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody UpdateManagerStatusReq request,
            @PathVariable Long spaceId,
            @PathVariable Long toDoItemId,
            @PathVariable Long userId
    ) {
        UpdateManagerStatusRes response =
                toDoItemService.updateManagerStatus(authUser.userId(), request, spaceId, toDoItemId, userId);
        return ApiSuccessRes.success(ApiSuccessCode.SUCCESS_UPDATE_MANAGER_STATUS, response);
    }

    @GetMapping
    public ResponseEntity<ApiSuccessRes<ToDoPageRes>> getToDoList(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long spaceId,
            @RequestParam int size,
            @RequestParam(defaultValue = "DESC") String direction,
            @RequestParam(required = false) Long cursor,
            @RequestParam(required = false) Long managerId,
            @RequestParam(required = false) String urgency,
            @RequestParam(defaultValue = "false") boolean includeExpired
    ) {
        CursorPageRequest request = CursorPageRequest.of(cursor, size, direction);
        ParamReq params = ParamReq.of(managerId, urgency, includeExpired);
        ToDoPageRes response = toDoItemService.getToDoList(authUser.userId(), spaceId, request, params);
        return ApiSuccessRes.success(ApiSuccessCode.SUCCESS_GET_TO_DO_LIST, response);
    }

    @GetMapping("/{toDoItemId}")
    public ResponseEntity<ApiSuccessRes<ToDoDetailRes>> getToBuy(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long spaceId,
            @PathVariable Long toDoItemId
    ) {
        ToDoDetailRes response = toDoItemService.getToDo(authUser.userId(), spaceId, toDoItemId);
        return ApiSuccessRes.success(ApiSuccessCode.SUCCESS_GET_TO_DO, response);
    }
}
