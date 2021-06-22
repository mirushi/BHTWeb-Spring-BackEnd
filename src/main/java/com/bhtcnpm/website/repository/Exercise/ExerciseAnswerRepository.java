package com.bhtcnpm.website.repository.Exercise;

import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseAnswerRepository extends JpaRepository<ExerciseAnswer, Long> {
}
