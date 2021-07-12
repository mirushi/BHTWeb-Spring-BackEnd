package com.bhtcnpm.website.service.Exercise;

import com.bhtcnpm.website.model.dto.Exercise.*;
import com.querydsl.core.types.Predicate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ExerciseService {
    List<ExerciseSummaryDTO> getExerciseList (Predicate predicate, Authentication authentication);
    List<ExerciseSummaryWithTopicDTO> getExerciseWithTopic (Predicate predicate, Authentication authentication);

    @PreAuthorize(value = "hasPermission(#id, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).EXERCISE_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.ExerciseActionPermissionRequest).READ_PERMISSION)")
    ExerciseDetailsDTO getExerciseDetails (Long id);

    @PreFilter(filterTarget = "exerciseIDs", value = "hasPermission(filterObject, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).EXERCISE_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.ExerciseActionPermissionRequest).READ_PERMISSION)")
    List<ExerciseStatisticDTO> getExerciseStatistics (List<Long> exerciseIDs);

    @PreFilter(filterTarget = "exerciseIDs", value = "hasPermission(filterObject, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).EXERCISE_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.ExerciseActionPermissionRequest).READ_PERMISSION)")
    List<ExerciseUserStatisticDTO> getExerciseUserStatistic (List<Long> exerciseIDs, Authentication authentication);

    @PreAuthorize(value = "hasRole(T(com.bhtcnpm.website.constant.security.permission.ExercisePermissionConstant).EXERCISE_ALL_ALL_CREATE)")
    ExerciseDetailsDTO createExercise (ExerciseRequestDTO dto, Authentication authentication);

    @PreAuthorize(value = "hasPermission(#exerciseID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).EXERCISE_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.ExerciseActionPermissionRequest).UPDATE_PERMISSION)")
    ExerciseDetailsDTO updateExercise (ExerciseRequestDTO dto, Long exerciseID, Authentication authentication);

    @PreAuthorize(value = "hasPermission(#exerciseID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).EXERCISE_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.ExerciseActionPermissionRequest).DELETE_PERMISSION)")
    void deleteExercise (Long exerciseID);

    List<ExerciseAvailableActionDTO> getAvailableExerciseAction (List<Long> exerciseIDs, Authentication authentication);

}
