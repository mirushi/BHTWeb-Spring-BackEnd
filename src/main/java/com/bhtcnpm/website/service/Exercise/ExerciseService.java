package com.bhtcnpm.website.service.Exercise;

import com.bhtcnpm.website.model.dto.Exercise.*;
import com.querydsl.core.types.Predicate;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ExerciseService {
    List<ExerciseSummaryDTO> getExerciseList (Predicate predicate, Authentication authentication);
    List<ExerciseSummaryWithTopicDTO> getExerciseWithTopic (Predicate predicate, Authentication authentication);
    ExerciseDetailsDTO getExerciseDetails (Long id);
    List<ExerciseStatisticDTO> getExerciseStatistics (List<Long> exerciseIDs);
    List<ExerciseUserStatisticDTO> getExerciseUserStatistic (List<Long> exerciseIDs, Authentication authentication);
    ExerciseDetailsDTO createExercise (ExerciseRequestDTO dto, Authentication authentication);
    ExerciseDetailsDTO updateExercise (ExerciseRequestDTO dto, Long exerciseID, Authentication authentication);
}
