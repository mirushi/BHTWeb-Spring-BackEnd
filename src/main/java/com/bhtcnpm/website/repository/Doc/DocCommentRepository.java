package com.bhtcnpm.website.repository.Doc;

import com.bhtcnpm.website.model.dto.DocComment.DocCommentDTO;
import com.bhtcnpm.website.model.dto.DocComment.DocCommentStatisticDTO;
import com.bhtcnpm.website.model.entity.DocCommentEntities.DocComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DocCommentRepository extends JpaRepository<DocComment, Long> {

    @Query("SELECT NEW com.bhtcnpm.website.model.dto.DocComment.DocCommentDTO(dc, COUNT(childComments.id)) " +
            "FROM DocComment dc " +
            "LEFT JOIN dc.childComments childComments " +
            "WHERE dc.doc.id = :docID AND dc.parentComment IS NULL " +
            "GROUP BY dc ")
    Page<DocCommentDTO> getDocCommentDTOsParentOnly (Long docID, Pageable pageable);

    List<DocComment> getDocCommentByParentCommentId (Long parentCommentId, Pageable pageable);

    @Query("SELECT NEW com.bhtcnpm.website.model.dto.DocComment.DocCommentStatisticDTO(dc.id, COUNT(DISTINCT uLiked.userDocCommentLikeId.user.id), " +
            "CASE WHEN EXISTS (SELECT 1 FROM dc.userDocCommentLikes uLikedSub WHERE uLikedSub.userDocCommentLikeId.user.id = :userID) THEN TRUE ELSE FALSE END) " +
            "FROM DocComment dc " +
            "LEFT JOIN dc.userDocCommentLikes uLiked " +
            "WHERE dc.id IN :commentIDs " +
            "GROUP BY dc.id ")
    List<DocCommentStatisticDTO> getDocCommentStatisticDTOs (List<Long> commentIDs, UUID userID);
}
