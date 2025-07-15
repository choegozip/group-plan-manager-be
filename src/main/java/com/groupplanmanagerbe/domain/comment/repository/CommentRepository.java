package com.groupplanmanagerbe.domain.comment.repository;

import com.groupplanmanagerbe.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
