package com.groupplanmanagerbe.domain.tobuycomment.repository;

import com.groupplanmanagerbe.domain.tobuycomment.entity.ToBuyComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<ToBuyComment, Long> {
}
