package com.bhtcnpm.website.repository.Exercise;

import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface ExerciseAttemptRepository extends JpaRepository<ExerciseAttempt, Long> {
    @Query("SELECT MAX(exerciseAttempt.totalScore) " +
            "FROM ExerciseAttempt exerciseAttempt " +
            "WHERE exerciseAttempt.user.id = :userID AND exerciseAttempt.exercise.id = :exerciseID")
    Integer findMaxScoreAttemptByUserInExercise (UUID userID, Long exerciseID);
}
