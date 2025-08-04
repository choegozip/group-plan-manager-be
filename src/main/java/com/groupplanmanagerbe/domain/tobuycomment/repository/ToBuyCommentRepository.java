package com.groupplanmanagerbe.domain.tobuycomment.repository;

import com.groupplanmanagerbe.domain.tobuycomment.entity.ToBuyComment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ToBuyCommentRepository extends JpaRepository<ToBuyComment, Long> {
    @EntityGraph(attributePaths = {"user"})
    List<ToBuyComment> findAllByToBuyItemId(Long toBuyItemId);
}
