package com.groupplanmanagerbe.domain.tobuycomment.repository;

import com.groupplanmanagerbe.domain.tobuycomment.entity.ToBuyComment;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ToBuyCommentRepository extends JpaRepository<ToBuyComment, Long> {
    @EntityGraph(attributePaths = {"user"})
    List<ToBuyComment> findAllByToBuyItemId(Long toBuyItemId);

    @Query("SELECT c FROM ToBuyComment c " +
            "WHERE c.id = :commentId " +
            "AND c.user.id = :userId " +
            "AND c.user.deleted = false")
    Optional<ToBuyComment> findByIdAndUserId(@Param("userId") Long userId,
                                       @Param("commentId") Long commentId);
}
