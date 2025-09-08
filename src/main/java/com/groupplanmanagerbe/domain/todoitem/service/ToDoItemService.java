package com.groupplanmanagerbe.domain.todoitem.service;

import com.groupplanmanagerbe.domain.space.entity.Space;
import com.groupplanmanagerbe.domain.space.service.SpaceComponent;
import com.groupplanmanagerbe.domain.todocomment.entity.ToDoComment;
import com.groupplanmanagerbe.domain.todocomment.service.ToDoCommentComponent;
import com.groupplanmanagerbe.domain.todoitem.entity.ToDoItem;
import com.groupplanmanagerbe.domain.todoitem.entity.ToDoManager;
import com.groupplanmanagerbe.domain.todoitem.event.ChangeToDoMgrStatusEvent;
import com.groupplanmanagerbe.domain.todoitem.event.CreateToDoEvent;
import com.groupplanmanagerbe.domain.todoitem.repository.ToDoItemRepository;
import com.groupplanmanagerbe.domain.user.entity.User;
import com.groupplanmanagerbe.domain.user.service.UserComponent;
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
import org.springframework.context.ApplicationEventPublisher;
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
    private final ToDoComponent toDoComponent;
    private final SpaceComponent spaceComponent;
    private final UserComponent userComponent;
    private final ToDoCommentComponent commentComponent;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public ToDoRes createToDo(Long userId, CreateToDoReq request, Long spaceId) {
        User user = userComponent.getByIdAndDeleteFalse(userId);
        Space space = spaceComponent.getByIdAndUserId(spaceId, userId, ApiErrorCode.SPACE_NOT_FOUND);
        ToDoItem toDo = createToDoItem(request, user, space);
        List<ToDoManager> managers = assignManagersToToDo(request.managerIds(), space, toDo);
        toDo.setManagers(managers);
        toDoItemRepository.save(toDo);

        publishCreateEvent(user.getNickname(), request.title(), managers);

        return ToDoRes.of(toDo.getId());
    }

    @Transactional
    public ToDoRes updateToDo(Long userId, UpdateToDoReq request, Long spaceId, Long toDoId) {
        ToDoItem toDo = toDoComponent.getByIdAndSpaceIdAndUserIdWithSpaceAndUser(toDoId, spaceId, userId);
        List<ToDoManager> managers = (request.managerIds() == null || request.managerIds().isEmpty())
                ? Collections.emptyList()
                : assignManagersToToDo(request.managerIds(), toDo.getSpace(), toDo);
        updateToDoItem(request, toDo, managers);
        return ToDoRes.of(toDo.getId());
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

        return UpdateManagerStatusRes.of(manager.getStatus());
    }

    public ToDoPageRes getToDoList(Long userId, Long spaceId, CursorPageRequest request, ParamReq params) {
        List<ToDoListProjection> toDoList = toDoComponent.getToDoItemsNative(spaceId, userId, params, request);
        Map<Long, List<ToDoManager>> managerMap = mapToDoManagersByItemId(toDoList);
        List<ToDoListRes> toDoListResList = toRes(toDoList, managerMap);
        return ToDoPageRes.of(toDoListResList, request.size());
    }

    public ToDoDetailRes getToDo(Long spaceId, Long toDoId) {
        ToDoItem toDo = toDoComponent.getByIdAndSpaceIdWithUser(toDoId, spaceId);
        List<ToDoComment> comments = commentComponent.getCommentList(toDoId);
        List<ToDoManager> managers = toDoComponent.getAllByToDoItemId(toDoId);
        return ToDoDetailRes.of(toDo, comments, managers);
    }

    // === Private Methods ===
    private void publishCreateEvent(String author, String item, List<ToDoManager> managers) {
        eventPublisher.publishEvent(new CreateToDoEvent(author, item, managers));
    }

    private void publishChangeStatusEvent(ToDoManager manager, UpdateManagerStatusReq request) {
        ToDoItem toDo  = manager.getToDoItem();
        eventPublisher.publishEvent(new ChangeToDoMgrStatusEvent(
                toDo.getUser().getId(), manager.getUser().getNickname(), toDo.getTitle(), request.managerStatus()));
    }

    private ToDoItem createToDoItem(CreateToDoReq request, User user, Space space) {
        return ToDoItem.of(space, user, request.title(), request.detail(), request.dueDate(), request.urgency(),
                request.imageUrl(), request.referenceUrl());
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

    private Map<Long, List<ToDoManager>> mapToDoManagersByItemId (List<ToDoListProjection> toDoList) {
        List<Long> toDoIds = toDoList.stream().map(ToDoListProjection::getToDoId).toList();
        List<ToDoManager> allManagers = toDoComponent.getByToDoItemIdsWithUser(toDoIds);
        return allManagers.stream()
                .collect(groupingBy(m -> m.getToDoItem().getId()));
    }

    private List<ToDoListRes> toRes(List<ToDoListProjection> toDoList, Map<Long, List<ToDoManager>> managerMap) {
        return toDoList.stream()
                .map(item -> ToDoListRes.of(item, managerMap.getOrDefault(item.getToDoId(), List.of())))
                .toList();
    }
}