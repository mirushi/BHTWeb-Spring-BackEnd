package com.bhtcnpm.website.repository;

import com.bhtcnpm.website.model.dto.Post.PostStatisticDTO;
import com.bhtcnpm.website.model.entity.PostEntities.Post;
import com.bhtcnpm.website.model.entity.enumeration.PostState.PostStateType;
import com.bhtcnpm.website.repository.custom.PostRepositoryCustom;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, Long>, QuerydslPredicateExecutor<Post>, PostRepositoryCustom {
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

    @Query("SELECT p " +
            "FROM Post p " +
            "LEFT JOIN FETCH p.tags tags " +
            "WHERE p.id = :id")
    Optional<Post> findByIDWithTags(Long id);

    boolean existsByCategoryId (Long postCategoryID);
}
