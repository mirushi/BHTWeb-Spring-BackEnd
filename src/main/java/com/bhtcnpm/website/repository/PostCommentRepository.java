package com.bhtcnpm.website.repository;

import com.bhtcnpm.website.model.dto.PostComment.PostCommentDTO;
import com.bhtcnpm.website.model.entity.PostEntities.PostComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
    //TODO: Please fix performance problems because we'll fetch all children comments with this.
    @Query("SELECT new com.bhtcnpm.website.model.dto.PostComment.PostCommentDTO(pc.id, pc.author.id, pc.author.avatarURL, pc.content, COUNT(childComments.id) ) " +
            "FROM PostComment pc " +
            "LEFT JOIN pc.childComments childComments " +
            "WHERE pc.post.id = :postID AND pc.parentComment IS NULL " +
            "GROUP BY pc ")
    Page<PostCommentDTO> getPostCommentDTOsParentOnly(Long postID, Pageable pageable);

    List<PostComment> getPostCommentByParentCommentId(Long parentCommentId);
}
