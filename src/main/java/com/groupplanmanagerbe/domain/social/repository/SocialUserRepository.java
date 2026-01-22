package com.groupplanmanagerbe.domain.social.repository;

import com.groupplanmanagerbe.domain.social.entity.SocialUser;
import com.groupplanmanagerbe.global.common.enums.SocialProvider;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SocialUserRepository extends JpaRepository<SocialUser, Long> {

    @EntityGraph(attributePaths = {"user"})
    @Query("""
            SELECT s FROM SocialUser s
            WHERE s.provider = :provider
            AND s.providerId = :providerId
            """)
    Optional<SocialUser> findByProviderAndProviderId(@Param("provider") SocialProvider provider,
                                                     @Param("providerId") String providerId);
}
