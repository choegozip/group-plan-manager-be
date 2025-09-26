package com.groupplanmanagerbe.domain.todoitem.service;

import com.groupplanmanagerbe.domain.space.entity.Space;
import com.groupplanmanagerbe.domain.space.service.SpaceComponent;
import com.groupplanmanagerbe.domain.todocomment.entity.ToDoComment;
import com.groupplanmanagerbe.domain.todocomment.service.ToDoCommentComponent;
import com.groupplanmanagerbe.domain.todoitem.entity.ToDoItem;
import com.groupplanmanagerbe.domain.todoitem.entity.ToDoManager;
import com.groupplanmanagerbe.global.alert.event.alert.AlertMgrStatusChangedEvent;
import com.groupplanmanagerbe.global.alert.event.alert.TdCreatedAlertEvent;
import com.groupplanmanagerbe.domain.todoitem.repository.ToDoItemRepository;
import com.groupplanmanagerbe.domain.user.entity.User;
import com.groupplanmanagerbe.domain.user.service.UserComponent;
import com.groupplanmanagerbe.global.alert.event.alert.TdUpdatedAlertEvent;
import com.groupplanmanagerbe.global.alert.event.refresh.RefreshEvent;
import com.groupplanmanagerbe.global.alert.listener.ItemManager;
import com.groupplanmanagerbe.global.common.enums.ApiErrorCode;
import com.groupplanmanagerbe.global.common.response.page.CursorPageRequest;
import com.groupplanmanagerbe.global.exception.custom.InvalidException;
import com.groupplanmanagerbe.presentation.tobuyitem.dto.request.ParamReq;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.groupingBy;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ToDoItemService {

    private final ToDoItemRepository toDoItemRepository;
    private final ToDoComponent toDoComponent;
    private final SpaceComponent spaceComponent;
    private final UserComponent userComponent;
    private final ToDoCommentComponent commentComponent;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public ToDoRes createToDo(Long userId, CreateToDoReq request, Long spaceId) {
        if (toDoComponent.countBySpaceId(spaceId) > 100) {
            throw new InvalidException(ApiErrorCode.TO_DO_LIMIT_EXCEEDED);
        }

        User user = userComponent.getByIdAndDeleteFalse(userId);
        Space space = spaceComponent.getByIdAndUserId(spaceId, userId, ApiErrorCode.SPACE_NOT_FOUND);
        ToDoItem toDo = createToDoItem(request, user, space);
        List<ToDoManager> managers = assignManagersToToDo(request.managerIds(), space, toDo);
        toDo.setManagers(managers);
        toDoItemRepository.save(toDo);

        publishCreateEvent(user.getNickname(), request.title(), managers);
        publishRefreshEvent(spaceId, userId);

        return ToDoRes.of(toDo.getId());
    }

    @Transactional
    public ToDoRes updateToDo(Long userId, UpdateToDoReq request, Long spaceId, Long toDoId) {
        ToDoItem todo = toDoComponent.getByIdAndSpaceIdAndUserIdWithSpaceAndUser(toDoId, spaceId, userId);

        alertNewManagers(todo, request.managerIds(), userId);

        List<ToDoManager> assignedManagers = assignManagersToToDo(request.managerIds(), todo.getSpace(), todo);
        updateToDoItem(request, todo, assignedManagers);

        return ToDoRes.of(todo.getId());
    }

    @Transactional
    public void deleteToDo(Long userId, Long spaceId, Long toDoId) {
        ToDoItem toDo = toDoComponent.getByIdAndSpaceIdAndUserId(toDoId, spaceId, userId);
        toDoItemRepository.delete(toDo);
    }

    @Transactional
    public UpdateManagerStatusRes updateManagerStatus(
            Long userId, UpdateManagerStatusReq request, Long spaceId, Long toDoId, Long managerId
    ) {
        if (!userId.equals(managerId)) {
            throw new InvalidException(ApiErrorCode.MANAGER_NOT_FOUND);
        }

        ToDoManager manager = toDoComponent.getByIdAndSpaceIdAndToDoIdWithToDo(managerId, spaceId, toDoId);
        manager.updateStatus(request.managerStatus());

        publishChangeStatusEvent(manager, request);
        publishRefreshEvent(spaceId, userId);

        return UpdateManagerStatusRes.of(manager.getStatus());
    }

    public ToDoPageRes getToDoList(Long userId, Long spaceId, CursorPageRequest request, ParamReq params) {
        List<ToDoListProjection> toDoList = toDoComponent.getToDoItemsNative(spaceId, userId, params, request);
        Map<Long, List<ToDoManager>> managerMap = mapToDoManagersByItemId(toDoList, userId);
        List<ToDoListRes> toDoListResList = toRes(toDoList, managerMap);
        return ToDoPageRes.of(toDoListResList, request.size());
    }

    public ToDoDetailRes getToDo(Long userId, Long spaceId, Long toDoId) {
        ToDoItem toDo = toDoComponent.getByIdAndSpaceIdWithUser(toDoId, spaceId);
        List<ToDoComment> comments = commentComponent.getCommentList(toDoId);
        List<ToDoManager> managers = toDoComponent.getAllByToDoItemId(toDoId, userId);
        return ToDoDetailRes.of(toDo, comments, managers);
    }

    // === Private Methods ===
    private void publishCreateEvent(String author, String item, List<ToDoManager> managers) {
        eventPublisher.publishEvent(new TdCreatedAlertEvent(author, item, managers, LocaleContextHolder.getLocale()));
    }

    private void publishUpdateEvent(String author, String item, List<ToDoManager> managers) {
        eventPublisher.publishEvent(new TdUpdatedAlertEvent(author, item, managers, LocaleContextHolder.getLocale()));
    }

    private void publishRefreshEvent(Long spaceId, Long actorId) {
        eventPublisher.publishEvent(new RefreshEvent(spaceId, actorId));
    }

    private void publishChangeStatusEvent(ToDoManager manager, UpdateManagerStatusReq request) {
        ToDoItem toDo  = manager.getToDoItem();
        eventPublisher.publishEvent(new AlertMgrStatusChangedEvent(
                toDo.getUser().getId(),
                manager.getUser().getNickname(),
                toDo.getTitle(),
                request.managerStatus(),
                LocaleContextHolder.getLocale()));
    }

    private ToDoItem createToDoItem(CreateToDoReq request, User user, Space space) {
        return ToDoItem.of(space, user, request.title(), request.detail(), request.dueDate(), request.urgency(),
                request.imageUrl(), request.referenceUrl());
    }

    private void alertNewManagers(ToDoItem todo, List<Long> requestManagerIds, Long actorId) {
        List<Long> newManagerIds = getNewMemberList(todo, requestManagerIds);
        if (!newManagerIds.isEmpty()) {
            List<ToDoManager> nesManagers = assignManagersToToDo(newManagerIds, todo.getSpace(), todo);
            publishUpdateEvent(todo.getUser().getNickname(), todo.getTitle(), nesManagers);
            publishRefreshEvent(todo.getSpace().getId(), actorId);
        }
    }

    private List<Long> getNewMemberList(ToDoItem toDo, List<Long> managerList) {
        List<Long> existingManagerId = toDo.getManagers().stream()
                .map(ItemManager::getUser)
                .map(User::getId)
                .toList();

        return managerList.stream()
                .filter(id -> !existingManagerId.contains(id))
                .toList();
    }

    private List<ToDoManager> assignManagersToToDo(List<Long> memberIds, Space space, ToDoItem toDoItem) {
        Set<Long> setMemberIds = Set.copyOf(memberIds);
        return space.getMembers().stream()
                .filter(member -> setMemberIds.contains(member.getUser().getId()))
                .map(member -> ToDoManager.of(member.getUser(), toDoItem))
                .toList();
    }

    private void updateToDoItem(UpdateToDoReq request, ToDoItem toDo, List<ToDoManager> managers) {
        toDo.updateToBuyItem(
                request.title(), request.detail(), request.dueDate(), request.urgency(),
                request.imageUrl(), request.referenceUrl(), managers);
    }

    private Map<Long, List<ToDoManager>> mapToDoManagersByItemId (List<ToDoListProjection> toDoList, Long userId) {
        List<Long> toDoIds = toDoList.stream().map(ToDoListProjection::getToDoId).toList();
        List<ToDoManager> allManagers = toDoComponent.getByToDoItemIdsWithUser(toDoIds, userId);
        return allManagers.stream()
                .collect(groupingBy(m -> m.getToDoItem().getId()));
    }

    private List<ToDoListRes> toRes(List<ToDoListProjection> toDoList, Map<Long, List<ToDoManager>> managerMap) {
        return toDoList.stream()
                .map(item -> ToDoListRes.of(item, managerMap.getOrDefault(item.getToDoId(), List.of())))
                .toList();
    }
}