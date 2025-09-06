package com.groupplanmanagerbe.domain.todoitem.service;

import com.groupplanmanagerbe.domain.todoitem.entity.ToDoItem;
import com.groupplanmanagerbe.domain.todoitem.entity.ToDoManager;
import com.groupplanmanagerbe.domain.todoitem.repository.ToDoItemRepository;
import com.groupplanmanagerbe.domain.todoitem.repository.ToDoManagerRepository;
import com.groupplanmanagerbe.global.common.enums.ApiErrorCode;
import com.groupplanmanagerbe.global.common.response.page.CursorPageRequest;
import com.groupplanmanagerbe.global.exception.custom.NotFoundException;
import com.groupplanmanagerbe.presentation.tobuyitem.dto.request.ParamReq;
import com.groupplanmanagerbe.presentation.todoitem.dto.ToDoListProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ToDoComponent {

    private final ToDoItemRepository toDoItemRepository;
    private final ToDoManagerRepository toDoManagerRepository;

    public ToDoItem getReferenceById(Long toDoId) {
        return toDoItemRepository.getReferenceById(toDoId);
    }

    public ToDoItem getByIdAndSpaceIdAndUserId(Long toDoId, Long spaceId, Long userId) {
        return toDoItemRepository.findByIdAndSpaceIdAndUserId(toDoId, spaceId, userId)
                .orElseThrow(() -> new NotFoundException(ApiErrorCode.TO_DO_NOT_FOUND));
    }

    public ToDoItem getByIdAndSpaceIdAndUserIdWithSpaceAndUser(Long toDoId, Long spaceId, Long userId) {
        return toDoItemRepository.findByIdAndSpaceIdAndUserIdWithSpaceAndUser(toDoId, spaceId, userId)
                .orElseThrow(() -> new NotFoundException(ApiErrorCode.TO_DO_NOT_FOUND));
    }

    public List<ToDoListProjection> getToDoItemsNative(
            Long spaceId, Long userId, ParamReq params, CursorPageRequest request) {
        return toDoItemRepository.findToDoItemsNative(
                spaceId, userId, params.managerId(), params.urgency(), request.cursor(),
                request.direction(), request.size(), params.includeExpired(), LocalDateTime.now());
    }

    public ToDoManager getByIdAndSpaceIdAndToDoIdWithToDo(Long managerId, Long spaceId, Long toDoId) {
        return toDoManagerRepository.findByIdAndSpaceIdAndToDoIdWithToDo(managerId, spaceId, toDoId)
                .orElseThrow(() -> new NotFoundException(ApiErrorCode.MANAGER_NOT_FOUND));
    }

    public List<ToDoManager> getByToDoItemIdsWithUser(List<Long> toBuyIds) {
        return toDoManagerRepository.findByToDoItemIdsWithUser(toBuyIds);
    }

    public List<ToDoManager> getAllByToDoItemId(Long toBuyId) {
        return toDoManagerRepository.findAllByToDoItemId(toBuyId);
    }
}
