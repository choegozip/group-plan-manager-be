package com.groupplanmanagerbe.domain.tobuycomment.service;

import com.groupplanmanagerbe.domain.tobuycomment.entity.ToBuyComment;
import com.groupplanmanagerbe.domain.tobuycomment.repository.ToBuyCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ToBuyCommentComponent {

    private final ToBuyCommentRepository commentRepository;

    public List<ToBuyComment> getCommentList(Long toBuyId) {
        return commentRepository.findAllByToBuyItemId(toBuyId);
    }
}
