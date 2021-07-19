package com.bhtcnpm.website.repository.Exercise.custom;

import com.bhtcnpm.website.model.dto.Exercise.ExerciseSummaryDTO;
import com.bhtcnpm.website.model.dto.Exercise.ExerciseSummaryWithTopicDTO;
import com.querydsl.core.types.Predicate;

import java.util.List;
import java.util.UUID;

public interface ExerciseRepositoryCustom {
    List<ExerciseSummaryDTO> getExerciseSummaryWithUserAttempts (Predicate predicate, UUID userID);
    List<ExerciseSummaryWithTopicDTO> getExerciseSummaryWithTopicAndUserAttempts (Predicate predicate, UUID userID);
}
