package com.bhtcnpm.website.repository;

import com.bhtcnpm.website.model.dto.Post.PostStatisticDTO;
import com.bhtcnpm.website.model.entity.PostEntities.Post;
import com.bhtcnpm.website.model.entity.enumeration.PostState.PostStateType;
import com.bhtcnpm.website.repository.custom.PostRepositoryCustom;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, Long>, QuerydslPredicateExecutor<Post>, JpaSpecificationExecutor<Post>, PostRepositoryCustom {
    @Query("SELECT new com.bhtcnpm.website.model.dto.Post.PostStatisticDTO(p.id, COUNT(DISTINCT pc.id) ,COUNT(DISTINCT uLiked.userPostLikeId.user.id), " +
            "CASE WHEN EXISTS (SELECT 1 FROM p.userPostLikes uLikedSub WHERE uLikedSub.userPostLikeId.user.id = :userID) THEN true ELSE false END, " +
            "CASE WHEN EXISTS (SELECT 1 FROM p.userPostSaves uSavedSub WHERE uSavedSub.userPostSaveId.user.id = :userID) THEN true ELSE false END) " +
            "FROM Post p " +
            "LEFT JOIN p.userPostLikes uLiked " +
            "LEFT JOIN PostComment pc ON p.id = pc.post.id " +
            "WHERE p.id IN :postIDs " +
            "GROUP BY p.id")
    List<PostStatisticDTO> getPostStatisticDTOs (List<Long> postIDs, UUID userID);

    @Modifying
    @Query("UPDATE Post as p " +
            "SET p.postState = :postStateType " +
            "WHERE p.id = :postID")
    int setPostState (Long postID, PostStateType postStateType);

    @Modifying
    @Query("UPDATE Post as p " +
            "SET p.postState = :postStateType, p.adminFeedback = :feedBack " +
            "WHERE p.id = :postID")
    int setPostStateAndFeedback (Long postID, PostStateType postStateType, String feedBack);

    List<Post> findByCategoryNameOrderByPublishDtmDesc (Predicate predicate, Pageable pageable, String categoryName);

    boolean existsByCategoryId (Long postCategoryID);
}
