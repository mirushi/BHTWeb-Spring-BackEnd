package com.bhtcnpm.website.repository.Post;

import com.bhtcnpm.website.model.entity.PostEntities.UserPostLike;
import com.bhtcnpm.website.model.entity.PostEntities.UserPostLikeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPostLikeRepository extends JpaRepository<UserPostLike, UserPostLikeId> {
    boolean existsByUserPostLikeId (UserPostLikeId userPostLikeId);
}
