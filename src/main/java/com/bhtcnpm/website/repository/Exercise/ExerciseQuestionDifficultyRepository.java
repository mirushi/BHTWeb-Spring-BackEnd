package com.bhtcnpm.website.repository.Exercise;

import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseQuestionDifficulty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseQuestionDifficultyRepository extends JpaRepository<ExerciseQuestionDifficulty, Integer> {
}
