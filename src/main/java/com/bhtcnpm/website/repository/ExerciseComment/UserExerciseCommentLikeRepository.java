package com.bhtcnpm.website.repository.ExerciseComment;

import com.bhtcnpm.website.model.entity.ExerciseEntities.UserExerciseCommentLike;
import com.bhtcnpm.website.model.entity.ExerciseEntities.UserExerciseCommentLikeId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserExerciseCommentLikeRepository extends JpaRepository<UserExerciseCommentLike, UserExerciseCommentLikeId> {
}
