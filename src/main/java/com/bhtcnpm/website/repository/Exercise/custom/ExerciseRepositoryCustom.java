package com.bhtcnpm.website.repository.Exercise.custom;

import com.bhtcnpm.website.model.dto.Exercise.ExerciseSummaryDTO;
import com.bhtcnpm.website.model.dto.Exercise.ExerciseSummaryWithTopicDTO;
import com.querydsl.core.types.Predicate;

import java.util.List;

public interface ExerciseRepositoryCustom {
    List<ExerciseSummaryDTO> getExerciseSummaryWithUserAttempts (Predicate predicate);
    List<ExerciseSummaryWithTopicDTO> getExerciseSummaryWithTopicAndUserAttempts (Predicate predicate);
}
