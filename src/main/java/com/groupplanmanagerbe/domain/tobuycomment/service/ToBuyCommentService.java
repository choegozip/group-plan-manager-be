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
import com.groupplanmanagerbe.presentation.comment.dto.request.CreateCommentReq;
import jakarta.persistence.EntityNotFoundException;
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
    public void createComment(Long userId, CreateCommentReq request, Long spaceId, Long toBuyId) {
        validateSpaceMembership(userId, spaceId);

        try {
            User user = userComponent.getByIdAndDeleteFalse(userId);
            ToBuyItem toBuyItem = toBuyComponent.getReferenceById(toBuyId);
            ToBuyComment comment = ToBuyComment.of(toBuyItem, user, request.content());

            commentRepository.save(comment);

        } catch (EntityNotFoundException | DataIntegrityViolationException e) {
            throw new NotFoundException(ApiErrorCode.TO_BUY_NOT_FOUND);
        }
    }

    private void validateSpaceMembership(Long userId, Long spaceId) {
        if (!spaceComponent.isSpaceMember(userId, spaceId)) {
            throw new InvalidException(ApiErrorCode.SPACE_NOT_FOUND);
        }
    }
}
