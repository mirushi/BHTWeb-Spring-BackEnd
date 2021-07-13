package com.bhtcnpm.website.repository.Exercise;

import com.bhtcnpm.website.model.dto.Exercise.ExerciseStatisticDTO;
import com.bhtcnpm.website.model.dto.Exercise.ExerciseSummaryDTO;
import com.bhtcnpm.website.model.dto.Exercise.ExerciseSummaryWithTopicDTO;
import com.bhtcnpm.website.model.dto.Exercise.ExerciseUserStatisticDTO;
import com.bhtcnpm.website.model.entity.ExerciseEntities.Exercise;
import com.querydsl.core.types.Predicate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExerciseRepository extends JpaRepository<Exercise, Long>, QuerydslPredicateExecutor<Exercise> {
    @Query("SELECT NEW com.bhtcnpm.website.model.dto.Exercise.ExerciseSummaryDTO(exercise.id, exercise.title, exercise.description, " +
            "CASE WHEN COUNT(DISTINCT attempts.id) > 0 THEN TRUE ELSE FALSE END," +
            "MAX(attempts.correctAnsweredQuestions)," +
            "COUNT(DISTINCT questions.id)) " +
            "FROM Exercise exercise " +
            "LEFT JOIN ExerciseAttempt attempts ON attempts.exercise.id = exercise.id " +
            "LEFT JOIN ExerciseQuestion questions ON questions.exercise.id = exercise.id " +
            "GROUP BY exercise " +
            "ORDER BY exercise.rank ASC")
    List<ExerciseSummaryDTO> getExerciseSummaryWithUserAttempts (Predicate predicate);

    @Query("SELECT NEW com.bhtcnpm.website.model.dto.Exercise.ExerciseSummaryWithTopicDTO(exercise.id, exercise.title, exercise.description, " +
            "CASE WHEN COUNT(DISTINCT attempts.id) > 0 THEN TRUE ELSE FALSE END, " +
            "MAX(attempts.correctAnsweredQuestions), " +
            "COUNT(DISTINCT questions.id), " +
            "exercise.topic.id, exercise.topic.name) " +
            "FROM Exercise exercise " +
            "LEFT JOIN exercise.exerciseAttempts attempts " +
            "LEFT JOIN exercise.exerciseQuestions questions " +
            "GROUP BY exercise " +
            "ORDER BY exercise.rank ASC")
    List<ExerciseSummaryWithTopicDTO> getExerciseSummaryWithTopicAndUserAttempts (Predicate predicate);

    @Query("SELECT exercise " +
            "FROM Exercise exercise " +
            "LEFT JOIN FETCH exercise.tags " +
            "WHERE exercise.id = :id")
    Optional<Exercise> findByIDWithTags (Long id);

    @Query(nativeQuery = true, value = "SELECT ex.id AS id, COUNT(DISTINCT questions.id) AS totalQuestions, COUNT(DISTINCT attempts.id) AS attemptCount, " +
            "CASE WHEN ex.SUGGESTED_DURATION > 0 THEN ex.SUGGESTED_DURATION ELSE (SELECT SUM(subQuestion.SUGGESTED_DURATION) FROM (EXERCISE_QUESTION subQuestion) WHERE subQuestion.EXERCISE_ID = ex.ID) END AS suggestedDuration " +
            "FROM EXERCISE ex " +
            "LEFT JOIN EXERCISE_ATTEMPT attempts ON ex.ID = attempts.EXERCISE_ID " +
            "LEFT JOIN EXERCISE_QUESTION questions ON ex.ID = questions.EXERCISE_ID " +
            "WHERE ex.id IN :exerciseIDs " +
            "GROUP BY ex.ID ")
    List<ExerciseStatisticDTO> getExercisesStatisticDTOs (List<Long> exerciseIDs);

    @Query("SELECT NEW com.bhtcnpm.website.model.dto.Exercise.ExerciseUserStatisticDTO(exercise.id, MAX(attempts.correctAnsweredQuestions), notes.note)" +
            "FROM Exercise exercise " +
            "LEFT JOIN exercise.exerciseAttempts attempts " +
            "LEFT JOIN exercise.exerciseNotes notes " +
            "WHERE exercise.id IN :exerciseIDs " +
            "AND (attempts.user.id IS NULL OR attempts.user.id = :userID) " +
            "AND (notes.exerciseNoteId.user.id IS NULL OR notes.exerciseNoteId.user.id = :userID)" +
            "GROUP BY exercise")
    List<ExerciseUserStatisticDTO> getExerciseUserStatisticDTOs (List<Long> exerciseIDs, UUID userID);
}
