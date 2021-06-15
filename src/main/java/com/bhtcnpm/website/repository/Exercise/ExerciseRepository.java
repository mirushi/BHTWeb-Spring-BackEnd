package com.bhtcnpm.website.repository.Exercise;

import com.bhtcnpm.website.model.dto.Exercise.ExerciseSummaryDTO;
import com.bhtcnpm.website.model.entity.ExerciseEntities.Exercise;
import com.querydsl.core.types.Predicate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.UUID;

public interface ExerciseRepository extends JpaRepository<Exercise, Long>, QuerydslPredicateExecutor<Exercise> {
    @Query("SELECT NEW com.bhtcnpm.website.model.dto.Exercise.ExerciseSummaryDTO(exercise.id, exercise.title, exercise.description, " +
            "CASE WHEN COUNT(DISTINCT attempts.id) > 0 THEN TRUE ELSE FALSE END) " +
            "FROM Exercise exercise " +
            "LEFT JOIN exercise.exerciseAttempts attempts " +
            "GROUP BY exercise")
    List<ExerciseSummaryDTO> getExerciseSummaryWithUserAttempt (Predicate predicate);
}
