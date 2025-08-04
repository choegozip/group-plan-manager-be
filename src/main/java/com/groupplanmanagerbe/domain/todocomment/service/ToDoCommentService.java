package com.groupplanmanagerbe.domain.todocomment.service;

import com.groupplanmanagerbe.domain.space.service.SpaceComponent;
import com.groupplanmanagerbe.domain.todocomment.entity.ToDoComment;
import com.groupplanmanagerbe.domain.todocomment.repository.ToDoCommentRepository;
import com.groupplanmanagerbe.domain.todoitem.entity.ToDoItem;
import com.groupplanmanagerbe.domain.todoitem.service.ToDoComponent;
import com.groupplanmanagerbe.domain.user.entity.User;
import com.groupplanmanagerbe.domain.user.service.UserComponent;
import com.groupplanmanagerbe.global.common.enums.ApiErrorCode;
import com.groupplanmanagerbe.global.exception.custom.InvalidException;
import com.groupplanmanagerbe.global.exception.custom.NotFoundException;
import com.groupplanmanagerbe.presentation.comment.dto.request.CreateCommentReq;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ToDoCommentService {

    private final ToDoCommentRepository commentRepository;
    private final UserComponent userComponent;
    private final SpaceComponent spaceComponent;
    private final ToDoComponent toDoComponent;

    @Transactional
    public void createComment(Long userId, CreateCommentReq request, Long spaceId, Long toDoId) {
        validateSpaceMembership(userId, spaceId);

        try {
            User user = userComponent.getByIdAndDeleteFalse(userId);
            ToDoItem toDo = toDoComponent.getReferenceById(toDoId);
            ToDoComment comment = ToDoComment.of(toDo, user, request.content());

            commentRepository.save(comment);

        } catch (EntityNotFoundException | DataIntegrityViolationException e) {
            throw new NotFoundException(ApiErrorCode.TO_DO_NOT_FOUND);
        }
    }

    private void validateSpaceMembership(Long userId, Long spaceId) {
        if (!spaceComponent.isSpaceMember(userId, spaceId)) {
            throw new InvalidException(ApiErrorCode.SPACE_NOT_FOUND);
        }
    }
}
