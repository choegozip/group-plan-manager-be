package com.groupplanmanagerbe.domain.space.repository;

import com.groupplanmanagerbe.domain.space.entity.Space;
import com.groupplanmanagerbe.domain.space.entity.SpaceMember;
import com.groupplanmanagerbe.global.common.response.page.CursorPageRequest;
import com.groupplanmanagerbe.presentation.space.dto.response.space.SpacesRes;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.*;
import org.hibernate.query.SortDirection;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SpaceRepositoryCustomImpl implements SpaceRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<SpacesRes> findSpacesWithCursor(CursorPageRequest request, Long userId) {
        // 1. 쿼리 빌더 가져오기
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        // 2. Space 엔티티를 조회하는 쿼리 생성 (SELECT)
        CriteriaQuery<Tuple> query = cb.createTupleQuery();
        // 3. FROM 절의 기준 테이블 설정
        Root<Space> space = query.from(Space.class);
        // 4. 조인 설정
        Join<Space, SpaceMember> member = space.join("members", JoinType.INNER);

        List<Predicate> predicates = new ArrayList<>();
        // 조건 1: WHERE m.user_id = :userId
        predicates.add(cb.equal(member.get("user").get("id"), userId));

        // 조건 2: 커서보다 작은 값들만 s.updated_at < :cursor
        if (!request.isFirstPage()) {
            predicates.add(cb.lessThan(space.get("updatedAt"), request.cursor()));
        }

        // 조건 3: 삭제되지 않은 스페이스만
        predicates.add(cb.equal(space.get("deleted"), false));

        // 모든 조건을 AND로 연결 WHERE m.user_id AND s.updated_at < :cursor
        query.where(predicates.toArray(new Predicate[0]));

        // 동적 정렬
        Order order = request.direction() == SortDirection.DESCENDING ?
                cb.desc(space.get(request.sortBy())) :
                cb.asc(space.get(request.sortBy()));
        query.orderBy(order);

        query.multiselect(
                space.get("id").alias("id"),
                space.get("name").alias("name"),
                space.get("profileImageKey").alias("profileImageKey"),
                member.get("isOwner").alias("isOwner"),
                space.get("createdAt").alias("createdAt"),
                space.get("updatedAt").alias("updatedAt")
        );

        return entityManager.createQuery(query)
                .setMaxResults(request.size() + 1)
                .getResultList()
                .stream()
                .map(tuple -> new SpacesRes(
                        tuple.get("id", Long.class),
                        tuple.get("name", String.class),
                        tuple.get("profileImageKey", String.class),
                        tuple.get("isOwner", Boolean.class),
                        tuple.get("createdAt", LocalDateTime.class),
                        tuple.get("updatedAt", LocalDateTime.class)
                ))
                .toList();
    }
}
