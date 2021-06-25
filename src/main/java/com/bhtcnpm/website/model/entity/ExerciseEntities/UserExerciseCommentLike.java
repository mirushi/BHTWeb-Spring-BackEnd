package com.bhtcnpm.website.model.entity.ExerciseEntities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "user_exercise_comment_like")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserExerciseCommentLike {
    @EmbeddedId
    private UserExerciseCommentLikeId userExerciseCommentLikeId;
}
