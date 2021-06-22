package com.bhtcnpm.website.repository.Exercise;

import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseAttemptRepository extends JpaRepository<ExerciseAttempt, Long> {
}
