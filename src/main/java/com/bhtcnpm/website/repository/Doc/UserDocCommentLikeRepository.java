package com.bhtcnpm.website.repository.Doc;

import com.bhtcnpm.website.model.entity.DocCommentEntities.UserDocCommentLike;
import com.bhtcnpm.website.model.entity.DocCommentEntities.UserDocCommentLikeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDocCommentLikeRepository extends JpaRepository<UserDocCommentLike, UserDocCommentLikeId> {
    long countByUserDocCommentLikeIdDocCommentId (Long docCommentID);
}
