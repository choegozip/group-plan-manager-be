package com.groupplanmanagerbe.domain.todocomment.service;

import com.groupplanmanagerbe.domain.todocomment.entity.ToDoComment;
import com.groupplanmanagerbe.domain.todocomment.repository.ToDoCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ToDoCommentComponent {

    private final ToDoCommentRepository commentRepository;

    public List<ToDoComment> getCommentList(Long toDoId) {
        return commentRepository.findAllByToDoItemId(toDoId);
    }
}
