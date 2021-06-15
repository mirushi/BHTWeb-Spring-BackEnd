package com.bhtcnpm.website.repository.Exercise;

import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseQuestionRepository extends JpaRepository<ExerciseQuestion, Long> {
}
