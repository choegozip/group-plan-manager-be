package com.groupplanmanagerbe.domain.todocomment.service;

import com.groupplanmanagerbe.domain.space.service.SpaceComponent;
import com.groupplanmanagerbe.domain.todocomment.entity.ToDoComment;
import com.groupplanmanagerbe.domain.todocomment.repository.ToDoCommentRepository;
import com.groupplanmanagerbe.domain.todoitem.entity.ToDoItem;
import com.groupplanmanagerbe.domain.todoitem.service.ToDoComponent;
import com.groupplanmanagerbe.domain.user.entity.User;
import com.groupplanmanagerbe.domain.user.service.UserComponent;
import com.groupplanmanagerbe.global.common.enums.ApiErrorCode;
import com.groupplanmanagerbe.global.common.response.page.CursorPageRequest;
import com.groupplanmanagerbe.global.exception.custom.InvalidException;
import com.groupplanmanagerbe.global.exception.custom.NotFoundException;
import com.groupplanmanagerbe.global.sse.SseService;
import com.groupplanmanagerbe.global.sse.dto.CommentData;
import com.groupplanmanagerbe.presentation.comment.dto.CommentListProjection;
import com.groupplanmanagerbe.presentation.comment.dto.request.CommentReq;
import com.groupplanmanagerbe.presentation.comment.dto.response.CommentListRes;
import com.groupplanmanagerbe.presentation.comment.dto.response.CommentPageRes;
import com.groupplanmanagerbe.presentation.comment.dto.response.CommentRes;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ToDoCommentService {

    private final ToDoCommentRepository commentRepository;
    private final UserComponent userComponent;
    private final SpaceComponent spaceComponent;
    private final ToDoComponent toDoComponent;
    private final SseService sseService;

    @Transactional
    public CommentRes createComment(Long userId, CommentReq request, Long spaceId, Long toDoId) {
        validateSpaceMembership(userId, spaceId);

        try {
            createEventIfFirstComment(spaceId, toDoId);

            User user = userComponent.getByIdAndDeleteFalse(userId);
            ToDoItem toDo = toDoComponent.getReferenceById(toDoId);
            ToDoComment comment = ToDoComment.of(toDo, user, request.content());

            commentRepository.save(comment);

            return CommentRes.of(comment.getId());

        } catch (EntityNotFoundException | DataIntegrityViolationException e) {
            throw new NotFoundException(ApiErrorCode.TO_DO_NOT_FOUND);
        }
    }

    @Transactional
    public CommentRes updateComment(Long userId, CommentReq request, Long toDoId, Long commentId) {
        ToDoComment comment = getComment(userId, commentId);
        validateCommentByToBuyId(comment, toDoId);
        comment.update(request.content());
        return CommentRes.of(comment.getId());
    }

    @Transactional
    public void deleteComment(Long userId, Long toBuyId, Long commentId) {
        ToDoComment comment = getComment(userId, commentId);
        validateCommentByToBuyId(comment, toBuyId);

        commentRepository.delete(comment);
    }

    public CommentPageRes getComments(Long userId, Long spaceId,  Long toDoId, CursorPageRequest request) {
        List<CommentListProjection> commentList = commentRepository.findCommentListNative(
                userId, spaceId, toDoId, request.cursor(), request.direction(), request.size());
        List<CommentListRes> commentListRes = commentList.stream()
                .map(CommentListRes::from)
                .toList();
        return CommentPageRes.of(commentListRes, request.size());
    }

    // ## private ##
    private void createEventIfFirstComment(Long spaceId, Long toDoId) {
        boolean existed = commentRepository.existComment(toDoId);
        if (!existed) {
            sseService.sendEvent(spaceId, sseService.TYPE_OF_TO_DO, CommentData.of(toDoId));
        }
    }

    private ToDoComment getComment(Long userId, Long commentId) {
        return commentRepository.findByIdAndUserId(userId, commentId)
                .orElseThrow(() -> new NotFoundException(ApiErrorCode.COMMENT_NOT_FOUND));
    }

    private void validateSpaceMembership(Long userId, Long spaceId) {
        if (!spaceComponent.isSpaceMember(userId, spaceId)) {
            throw new InvalidException(ApiErrorCode.SPACE_NOT_FOUND);
        }
    }

    private void validateCommentByToBuyId(ToDoComment comment, Long toBuyId) {
        if (!comment.getToDoItem().getId().equals(toBuyId)) {
            throw new NotFoundException(ApiErrorCode.TO_DO_NOT_FOUND);
        }
    }
}
