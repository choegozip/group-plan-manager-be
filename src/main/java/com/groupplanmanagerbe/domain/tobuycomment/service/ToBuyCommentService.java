package com.groupplanmanagerbe.domain.tobuycomment.service;

import com.groupplanmanagerbe.domain.tobuycomment.repository.ToBuyCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ToBuyCommentService {
    private final ToBuyCommentRepository commentRepository;
}
