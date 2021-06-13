package com.bhtcnpm.website.repository;

import com.bhtcnpm.website.model.entity.PostEntities.PostView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface PostViewRepository extends JpaRepository<PostView, Long> {
    @Query("SELECT CASE WHEN (COUNT(pv) > 0) THEN TRUE ELSE FALSE END " +
            "FROM PostView pv " +
            "WHERE pv.post.id = :postID AND (pv.user.id = :userID OR pv.ipAddress = :ipAddress AND pv.user.id IS NULL)")
    boolean existsByPostIdAndUserIdOrIpAddress (Long postID, UUID userID, String ipAddress);
}
