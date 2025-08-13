package com.groupplanmanagerbe.domain.todoitem.service;

import com.groupplanmanagerbe.domain.space.entity.Space;
import com.groupplanmanagerbe.domain.space.service.SpaceComponent;
import com.groupplanmanagerbe.domain.todocomment.entity.ToDoComment;
import com.groupplanmanagerbe.domain.todocomment.service.ToDoCommentComponent;
import com.groupplanmanagerbe.domain.todoitem.entity.ToDoItem;
import com.groupplanmanagerbe.domain.todoitem.entity.ToDoManager;
import com.groupplanmanagerbe.domain.todoitem.repository.ToDoItemRepository;
import com.groupplanmanagerbe.domain.todoitem.repository.ToDoManagerRepository;
import com.groupplanmanagerbe.domain.user.entity.User;
import com.groupplanmanagerbe.domain.user.service.UserComponent;
import com.groupplanmanagerbe.global.common.enums.ApiErrorCode;
import com.groupplanmanagerbe.global.common.response.page.CursorPageRequest;
import com.groupplanmanagerbe.global.exception.custom.InvalidException;
import com.groupplanmanagerbe.global.exception.custom.NotFoundException;
import com.groupplanmanagerbe.presentation.tobuyitem.dto.request.UpdateManagerStatusReq;
import com.groupplanmanagerbe.presentation.tobuyitem.dto.response.UpdateManagerStatusRes;
import com.groupplanmanagerbe.presentation.todoitem.dto.ToDoListProjection;
import com.groupplanmanagerbe.presentation.todoitem.dto.request.CreateToDoReq;
import com.groupplanmanagerbe.presentation.todoitem.dto.request.UpdateToDoReq;
import com.groupplanmanagerbe.presentation.todoitem.dto.response.ToDoDetailRes;
import com.groupplanmanagerbe.presentation.todoitem.dto.response.ToDoListRes;
import com.groupplanmanagerbe.presentation.todoitem.dto.response.ToDoPageRes;
import com.groupplanmanagerbe.presentation.todoitem.dto.response.ToDoRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.groupingBy;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ToDoItemService {

    private final ToDoItemRepository toDoItemRepository;
    private final ToDoManagerRepository toDoManagerRepository;
    private final SpaceComponent spaceComponent;
    private final UserComponent userComponent;
    private final ToDoCommentComponent commentComponent;

    @Transactional
    public ToDoRes createToDo(Long userId, CreateToDoReq request, Long spaceId) {
        User user = userComponent.getByIdAndDeleteFalse(userId);
        Space space = spaceComponent.getByIdAndUserId(spaceId, userId, ApiErrorCode.SPACE_NOT_FOUND);

        ToDoItem toDo = ToDoItem.of(
                space, user, request.title(), request.detail(), request.dueDate(),
                request.urgency(), request.imageUrl(), request.referenceUrl());

        List<ToDoManager> managers = createToBuy(request.managerIds(), space, toDo);
        toDo.setManagers(managers);

        toDoItemRepository.save(toDo);

        return ToDoRes.of(toDo.getId());
    }

    @Transactional
    public ToDoRes updateToDo(Long userId, UpdateToDoReq request, Long spaceId, Long toDoId) {
        ToDoItem toDo = toDoItemRepository.findByIdAndUserIdWithSpaceAndUser(toDoId, userId)
                .orElseThrow(() -> new NotFoundException(ApiErrorCode.TO_DO_NOT_FOUND));
        validateSpaceId(toDo, spaceId);

        List<ToDoManager> managers = Collections.emptyList();
        if (request.managerIds() != null && !request.managerIds().isEmpty()) {
            managers = createToBuy(request.managerIds(), toDo.getSpace(), toDo);
        }

        toDo.updateToBuyItem(
                request.title(), request.detail(), request.dueDate(), request.urgency(),
                request.imageUrl(), request.referenceUrl(), managers);

        return ToDoRes.of(toDo.getId());
    }

    @Transactional
    public void deleteToDo(Long userId, Long spaceId, Long toDoId) {
        ToDoItem toDo = toDoItemRepository.findByIdAndUserId(toDoId, userId)
                .orElseThrow(() -> new NotFoundException(ApiErrorCode.TO_DO_NOT_FOUND));
        validateSpaceId(toDo, spaceId);

        toDoItemRepository.delete(toDo);
    }

    @Transactional
    public UpdateManagerStatusRes updateManagerStatus(
            Long userId, UpdateManagerStatusReq request, Long spaceId, Long toBuyItemId, Long managerId
    ) {
        ToDoManager manager = toDoManagerRepository.findByIdAndUserIdWithToDoAndSpace(managerId, userId)
                .orElseThrow(() -> new NotFoundException(ApiErrorCode.MANAGER_NOT_FOUND));
        validateToDoManager(manager, spaceId, toBuyItemId);
        manager.updateStatus(request.managerStatus());
        return UpdateManagerStatusRes.of(manager.getStatus());
    }

    public ToDoPageRes getToDoList(Long userId, Long spaceId, CursorPageRequest request) {
        List<ToDoListProjection> toDoList = toDoItemRepository.findToDoItemsNative(
                spaceId, userId, request.managerId(), request.urgency(), request.cursor(),
                request.direction(), request.size());

        Map<Long, List<ToDoManager>> managerMap = mapToDoManagersByItemId (toDoList);
        List<ToDoListRes> toDoListResList = toDoList.stream()
                .map(item -> ToDoListRes.of(item, managerMap.getOrDefault(item.getToDoId(), List.of())))
                .toList();

        return ToDoPageRes.of(toDoListResList, request.size());
    }

    public ToDoDetailRes getToDo(Long userId, Long spaceId, Long toDoId) {
        ToDoItem toDo = toDoItemRepository.findByIdWithSpaceAndUser(toDoId)
                .orElseThrow(() -> new NotFoundException(ApiErrorCode.TO_DO_NOT_FOUND));
        validateSpaceId(toDo, spaceId);
        validateMemberId(toDo, userId);

        List<ToDoComment> comments = commentComponent.getCommentList(toDoId);
        List<ToDoManager> managers = toDoManagerRepository.findAllByToDoItemId(toDoId);
        return ToDoDetailRes.of(toDo, comments, managers);
    }

    // === Private Methods ===
    private List<ToDoManager> createToBuy(List<Long> memberIds, Space space, ToDoItem toDoItem) {
        Set<Long> setMemberIds = Set.copyOf(memberIds);
        return space.getMembers().stream()
                .filter(member -> setMemberIds.contains(member.getUser().getId()))
                .map(member -> ToDoManager.of(member.getUser(), toDoItem))
                .toList();
    }

    private Map<Long, List<ToDoManager>> mapToDoManagersByItemId (List<ToDoListProjection> toDoList) {
        List<Long> toDoIds = toDoList.stream().map(ToDoListProjection::getToDoId).toList();
        List<ToDoManager> allManagers = toDoManagerRepository.findByToDoItemIdsWithUser(toDoIds);
        return allManagers.stream()
                .collect(groupingBy(m -> m.getToDoItem().getId()));
    }

    private void validateSpaceId(ToDoItem item, Long spaceId) {
        if (!item.getSpace().getId().equals(spaceId)) {
            throw new InvalidException(ApiErrorCode.INVALID_SPACE_ID);
        }
    }

    private void validateToDoManager(ToDoManager manager, Long spaceId, Long toDoId) {
        if (!manager.getToDoItem().getSpace().getId().equals(spaceId)) {
            throw new InvalidException(ApiErrorCode.INVALID_SPACE_ID);
        }

        if (!manager.getToDoItem().getId().equals(toDoId)) {
            throw new InvalidException(ApiErrorCode.INVALID_TO_DO_ID);
        }
    }

    private void validateMemberId(ToDoItem toDo, Long userId) {
        boolean isMember = toDo.getSpace().getMembers().stream()
                .anyMatch(member -> member.getUser().getId().equals(userId));
        if (!isMember) {
            throw new InvalidException(ApiErrorCode.TO_BUY_NOT_FOUND);
        }
    }
}