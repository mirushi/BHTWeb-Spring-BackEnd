package com.bhtcnpm.website.repository.ExerciseComment;

import com.bhtcnpm.website.model.dto.ExerciseComment.ExerciseCommentDTO;
import com.bhtcnpm.website.model.dto.ExerciseComment.ExerciseCommentStatisticDTO;
import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ExerciseCommentRepository extends JpaRepository<ExerciseComment, Long> {
    @Query("SELECT NEW com.bhtcnpm.website.model.dto.ExerciseComment.ExerciseCommentDTO(ec, COUNT(childComments.id)) " +
            "FROM ExerciseComment ec " +
            "LEFT JOIN ec.childComments childComments " +
            "WHERE ec.exercise.id = :exerciseID AND ec.parentComment IS NULL " +
            "GROUP BY ec ")
    Page<ExerciseCommentDTO> getExerciseCommentDTOsParentOnly (Long exerciseID, Pageable pageable);

    List<ExerciseComment> getExerciseCommentByParentCommentId (Long parentCommentId, Pageable pageable);

    @Query("SELECT NEW com.bhtcnpm.website.model.dto.ExerciseComment.ExerciseCommentStatisticDTO(ec.id, COUNT(DISTINCT uLiked.userExerciseCommentLikeId.user.id), " +
            "CASE WHEN EXISTS (SELECT 1 FROM ec.userExerciseCommentLikes uLikedSub WHERE uLikedSub.userExerciseCommentLikeId.user.id = :userID) THEN TRUE ELSE FALSE END) " +
            "FROM ExerciseComment ec " +
            "LEFT JOIN ec.userExerciseCommentLikes uLiked " +
            "WHERE ec.id IN :commentIDs " +
            "GROUP BY ec.id")
    List<ExerciseCommentStatisticDTO> getExerciseCommentStatisticDTOs (List<Long> commentIDs, UUID userID);
}
