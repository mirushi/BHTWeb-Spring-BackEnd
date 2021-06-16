package com.bhtcnpm.website.repository.Exercise;

import com.bhtcnpm.website.model.dto.Exercise.ExerciseDetailsDTO;
import com.bhtcnpm.website.model.dto.Exercise.ExerciseStatisticDTO;
import com.bhtcnpm.website.model.dto.Exercise.ExerciseSummaryDTO;
import com.bhtcnpm.website.model.dto.Exercise.ExerciseSummaryWithTopicDTO;
import com.bhtcnpm.website.model.entity.ExerciseEntities.Exercise;
import com.querydsl.core.types.Predicate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;

public interface ExerciseRepository extends JpaRepository<Exercise, Long>, QuerydslPredicateExecutor<Exercise> {
    @Query("SELECT NEW com.bhtcnpm.website.model.dto.Exercise.ExerciseSummaryDTO(exercise.id, exercise.title, exercise.description, " +
            "CASE WHEN COUNT(DISTINCT attempts.id) > 0 THEN TRUE ELSE FALSE END) " +
            "FROM Exercise exercise " +
            "LEFT JOIN exercise.exerciseAttempts attempts " +
            "GROUP BY exercise " +
            "ORDER BY exercise.rank ASC")
    List<ExerciseSummaryDTO> getExerciseSummaryWithUserAttempts (Predicate predicate);

    @Query("SELECT NEW com.bhtcnpm.website.model.dto.Exercise.ExerciseSummaryWithTopicDTO(exercise.id, exercise.title, exercise.description, exercise.topic.id, exercise.topic.name, " +
            "CASE WHEN COUNT(DISTINCT attempts.id) > 0 THEN TRUE ELSE FALSE END) " +
            "FROM Exercise exercise " +
            "LEFT JOIN exercise.exerciseAttempts attempts " +
            "GROUP BY exercise " +
            "ORDER BY exercise.rank ASC")
    List<ExerciseSummaryWithTopicDTO> getExerciseSummaryWithTopicAndUserAttempts (Predicate predicate);

    @Query("SELECT exercise " +
            "FROM Exercise exercise " +
            "LEFT JOIN FETCH exercise.tags " +
            "WHERE exercise.id = :id")
    Optional<Exercise> findByIDWithTags (Long id);

    @Query("SELECT NEW com.bhtcnpm.website.model.dto.Exercise.ExerciseStatisticDTO(exercise.id, COUNT(DISTINCT questions.id), COUNT(DISTINCT attempts.id)) " +
            "FROM Exercise exercise " +
            "LEFT JOIN exercise.exerciseAttempts attempts " +
            "LEFT JOIN exercise.exerciseQuestions questions " +
            "WHERE exercise.id IN :exerciseIDs " +
            "GROUP BY exercise ")
    List<ExerciseStatisticDTO> getExercisesStatisticDTOs (List<Long> exerciseIDs);

}
