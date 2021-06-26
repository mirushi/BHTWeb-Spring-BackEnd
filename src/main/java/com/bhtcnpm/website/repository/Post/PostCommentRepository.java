package com.bhtcnpm.website.repository.Post;

import com.bhtcnpm.website.model.dto.PostComment.PostCommentDTO;
import com.bhtcnpm.website.model.dto.PostComment.PostCommentStatisticDTO;
import com.bhtcnpm.website.model.entity.PostCommentEntities.PostComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
    @Query("SELECT new com.bhtcnpm.website.model.dto.PostComment.PostCommentDTO(pc, COUNT(childComments.id)) " +
            "FROM PostComment pc " +
            "LEFT JOIN pc.childComments childComments " +
            "WHERE pc.post.id = :postID AND pc.parentComment IS NULL " +
            "GROUP BY pc ")
    Page<PostCommentDTO> getPostCommentDTOsParentOnly(Long postID, Pageable pageable);

    List<PostComment> getPostCommentByParentCommentId(Long parentCommentId, Pageable pageable);

    @Query("SELECT new com.bhtcnpm.website.model.dto.PostComment.PostCommentStatisticDTO(pc.id, COUNT(DISTINCT uLiked.userPostCommentLikeId.user.id), " +
            "CASE WHEN EXISTS (SELECT 1 FROM pc.userPostCommentLikes uLikedSub WHERE uLikedSub.userPostCommentLikeId.user.id = :userID) THEN true ELSE false END) " +
            "FROM PostComment pc " +
            "LEFT JOIN pc.userPostCommentLikes uLiked " +
            "WHERE pc.id IN :commentIDs " +
            "GROUP BY pc.id")
    List<PostCommentStatisticDTO> getPostCommentStatisticDTOs (List<Long> commentIDs, UUID userID);
}
