package com.bhtcnpm.website.repository.Post;

import com.bhtcnpm.website.model.entity.PostCommentEntities.UserPostCommentLike;
import com.bhtcnpm.website.model.entity.PostCommentEntities.UserPostCommentLikeId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPostCommentLikeRepository extends JpaRepository<UserPostCommentLike, UserPostCommentLikeId> {
}
