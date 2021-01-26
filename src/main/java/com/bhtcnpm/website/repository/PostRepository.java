package com.bhtcnpm.website.repository;

import com.bhtcnpm.website.model.dto.Post.PostStatisticDTO;
import com.bhtcnpm.website.model.dto.Post.PostSummaryDTO;
import com.bhtcnpm.website.model.entity.PostEntities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT new com.bhtcnpm.website.model.dto.Post.PostStatisticDTO(p.id, COUNT(DISTINCT pc.id) ,COUNT(DISTINCT uLiked.id), " +
            "CASE WHEN EXISTS (SELECT uLiked.id FROM uLiked WHERE uLiked.id = :userID) THEN true ELSE false END, " +
            "CASE WHEN EXISTS (SELECT uSaved.id FROM uSaved WHERE uSaved.id = :userID) THEN true ELSE false END) " +
            "FROM Post p " +
            "JOIN p.usersLiked uLiked " +
            "JOIN p.usersSaved uSaved " +
            "JOIN PostComment pc ON p.id = pc.post.id " +
            "WHERE p.id IN :postIDs " +
            "GROUP BY p.id")
    List<PostStatisticDTO> getPostStatisticDTOs (List<Long> postIDs, Long userID);

}
