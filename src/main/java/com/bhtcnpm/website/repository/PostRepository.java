package com.bhtcnpm.website.repository;

import com.bhtcnpm.website.model.dto.Post.PostStatisticDTO;
import com.bhtcnpm.website.model.dto.Post.PostSummaryDTO;
import com.bhtcnpm.website.model.entity.PostEntities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>, QuerydslPredicateExecutor<Post> {
    @Query("SELECT new com.bhtcnpm.website.model.dto.Post.PostStatisticDTO(p.id, COUNT(DISTINCT pc.id) ,COUNT(DISTINCT uLiked.id), " +
            "CASE WHEN EXISTS (SELECT uLikedSub.id FROM p.usersLiked uLikedSub WHERE uLikedSub.id = :userID) THEN true ELSE false END, " +
            "CASE WHEN EXISTS (SELECT uSavedSub.id FROM p.usersSaved uSavedSub WHERE uSavedSub.id = :userID) THEN true ELSE false END) " +
            "FROM Post p " +
            "LEFT JOIN p.usersLiked uLiked " +
            "LEFT JOIN p.usersSaved uSaved " +
            "LEFT JOIN PostComment pc ON p.id = pc.post.id " +
            "WHERE p.id IN :postIDs " +
            "GROUP BY p.id")
    List<PostStatisticDTO> getPostStatisticDTOs (List<Long> postIDs, Long userID);
}
