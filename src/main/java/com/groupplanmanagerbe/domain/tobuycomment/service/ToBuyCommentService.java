package com.groupplanmanagerbe.domain.tobuycomment.service;

import com.groupplanmanagerbe.domain.space.service.SpaceComponent;
import com.groupplanmanagerbe.domain.tobuycomment.entity.ToBuyComment;
import com.groupplanmanagerbe.domain.tobuycomment.repository.ToBuyCommentRepository;
import com.groupplanmanagerbe.domain.tobuyitem.entity.ToBuyItem;
import com.groupplanmanagerbe.domain.tobuyitem.service.ToBuyComponent;
import com.groupplanmanagerbe.domain.user.entity.User;
import com.groupplanmanagerbe.domain.user.service.UserComponent;
import com.groupplanmanagerbe.global.common.enums.ApiErrorCode;
import com.groupplanmanagerbe.global.exception.custom.InvalidException;
import com.groupplanmanagerbe.global.exception.custom.NotFoundException;
import com.groupplanmanagerbe.presentation.comment.dto.request.CommentReq;
import com.groupplanmanagerbe.presentation.comment.dto.response.CommentRes;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ToBuyCommentService {

    private final ToBuyCommentRepository commentRepository;
    private final UserComponent userComponent;
    private final SpaceComponent spaceComponent;
    private final ToBuyComponent toBuyComponent;

    @Transactional
    public CommentRes createComment(Long userId, CommentReq request, Long spaceId, Long toBuyId) {
        validateSpaceMembership(userId, spaceId);

        try {
            User user = userComponent.getByIdAndDeleteFalse(userId);
            ToBuyItem toBuyItem = toBuyComponent.getReferenceById(toBuyId);
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
