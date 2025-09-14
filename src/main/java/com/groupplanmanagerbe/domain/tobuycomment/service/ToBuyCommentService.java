package com.groupplanmanagerbe.domain.tobuycomment.service;

import com.groupplanmanagerbe.domain.space.service.SpaceComponent;
import com.groupplanmanagerbe.domain.tobuycomment.entity.ToBuyComment;
import com.groupplanmanagerbe.domain.tobuycomment.repository.ToBuyCommentRepository;
import com.groupplanmanagerbe.domain.tobuyitem.entity.ToBuyItem;
import com.groupplanmanagerbe.domain.tobuyitem.service.ToBuyComponent;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ToBuyCommentService {

    private final ToBuyCommentRepository commentRepository;
    private final UserComponent userComponent;
    private final SpaceComponent spaceComponent;
    private final ToBuyComponent toBuyComponent;
    private final SseService sseService;

    @Transactional
    public CommentRes createComment(Long userId, CommentReq request, Long spaceId, Long toBuyId) {
        validateSpaceMembership(userId, spaceId);

        try {
            createEventIfFirstComment(spaceId, toBuyId);

            User user = userComponent.getByIdAndDeleteFalse(userId);
            ToBuyItem toBuyItem = toBuyComponent.getReferenceById(toBuyId);
            toBuyItem.addComment(ToBuyComment.of(toBuyItem, user, request.content()));
            ToBuyComment comment = ToBuyComment.of(toBuyItem, user, request.content());

            commentRepository.save(comment);

            return CommentRes.of(comment.getId());

        } catch (EntityNotFoundException | DataIntegrityViolationException e) {
            throw new NotFoundException(ApiErrorCode.TO_BUY_NOT_FOUND);
        }
    }

    @Transactional
    public CommentRes updateComment(Long userId, CommentReq request, Long toBuyId, Long commentId) {
        ToBuyComment comment = getComment(userId, commentId);
        validateCommentByToBuyId(comment, toBuyId);
        comment.update(request.content());
        return CommentRes.of(comment.getId());
    }

    @Transactional
    public void deleteComment(Long userId, Long toBuyId, Long commentId) {
        ToBuyComment comment = getComment(userId, commentId);
        validateCommentByToBuyId(comment, toBuyId);

        commentRepository.delete(comment);
    }

    public CommentPageRes getComments(Long userId, Long spaceId, Long toBuyId, CursorPageRequest request) {
        List<CommentListProjection> commentList = commentRepository.findCommentListNative(
                userId, spaceId, toBuyId, request.cursor(), request.direction(), request.size());
        List<CommentListRes> commentListRes = commentList.stream()
                .map(CommentListRes::from)
                .toList();
        return CommentPageRes.of(commentListRes, request.size());
    }

    // ## private ##
    private void createEventIfFirstComment(Long spaceId, Long toBuyId) {
        boolean existed = commentRepository.existComment(toBuyId);
        if (!existed) {
            sseService.sendEvent(spaceId, sseService.TYPE_OF_TO_BUY, CommentData.of(toBuyId));
        }
    }

    private ToBuyComment getComment(Long userId, Long commentId) {
        return commentRepository.findByIdAndUserId(userId, commentId)
                .orElseThrow(() -> new NotFoundException(ApiErrorCode.COMMENT_NOT_FOUND));
    }

    private void validateSpaceMembership(Long userId, Long spaceId) {
        if (!spaceComponent.isSpaceMember(userId, spaceId)) {
            throw new InvalidException(ApiErrorCode.SPACE_NOT_FOUND);
        }
    }

    private void validateCommentByToBuyId(ToBuyComment comment, Long toBuyId) {
        if (!comment.getToBuyItem().getId().equals(toBuyId)) {
            throw new NotFoundException(ApiErrorCode.TO_BUY_NOT_FOUND);
        }
    }
}
