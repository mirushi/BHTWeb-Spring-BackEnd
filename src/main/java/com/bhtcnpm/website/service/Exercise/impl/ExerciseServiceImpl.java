package com.bhtcnpm.website.service.Exercise.impl;

import com.bhtcnpm.website.constant.business.Exercise.ExerciseActionAvailableConstant;
import com.bhtcnpm.website.constant.security.evaluator.permission.ExerciseActionPermissionRequest;
import com.bhtcnpm.website.model.dto.Exercise.*;
import com.bhtcnpm.website.model.dto.Exercise.mapper.ExerciseMapper;
import com.bhtcnpm.website.model.entity.ExerciseEntities.Exercise;
import com.bhtcnpm.website.model.exception.IDNotFoundException;
import com.bhtcnpm.website.repository.Exercise.ExerciseAttemptRepository;
import com.bhtcnpm.website.repository.Exercise.ExerciseRepository;
import com.bhtcnpm.website.security.evaluator.Exercise.ExercisePermissionEvaluator;
import com.bhtcnpm.website.security.predicate.Exercise.ExerciseOrderingGenerator;
import com.bhtcnpm.website.security.predicate.Exercise.ExercisePredicateGenerator;
import com.bhtcnpm.website.security.util.SecurityUtils;
import com.bhtcnpm.website.service.Exercise.ExerciseService;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.Validate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ExerciseServiceImpl implements ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final ExerciseAttemptRepository exerciseAttemptRepository;
    private final ExerciseMapper exerciseMapper;
    private final ExercisePermissionEvaluator exercisePermissionEvaluator;

    @Override
    //TODO: The predicate will not be applied. Fix this when you have time.
    public List<ExerciseSummaryDTO> getExerciseList(Predicate predicate, Pageable pageable, Authentication authentication) {
        UUID userID = SecurityUtils.getUserID(authentication);

        Sort sortByRankAsc = ExerciseOrderingGenerator.orderByRankAsc();

        List<ExerciseSummaryDTO> exerciseSummaryDTOList;

        if (userID == null) {
            Iterable<Exercise> exerciseIterable = exerciseRepository.findAll(predicate, sortByRankAsc);
            exerciseSummaryDTOList = exerciseMapper.exerciseIterableToExerciseSummaryDTOList(exerciseIterable);
        } else {
            exerciseSummaryDTOList = exerciseRepository.getExerciseSummaryWithUserAttempts(userID);
        }

        return exerciseSummaryDTOList;
    }

    @Override
    //TODO: The predicate will not be applied. Fix this when you have time.
    public List<ExerciseSummaryWithTopicDTO> getExerciseWithTopic(Predicate predicate, Authentication authentication) {
        UUID userID = SecurityUtils.getUserID(authentication);
        Sort sortByRankAsc = ExerciseOrderingGenerator.orderByRankAsc();

        List<ExerciseSummaryWithTopicDTO> exerciseSummaryWithTopicDTOList;

        if (userID == null) {
            Iterable<Exercise> exerciseIterable = exerciseRepository.findAll(predicate, sortByRankAsc);
            exerciseSummaryWithTopicDTOList = exerciseMapper.exerciseIterableToExerciseSummaryWithTopicDTOList(exerciseIterable);
        } else {
            exerciseSummaryWithTopicDTOList = exerciseRepository.getExerciseSummaryWithTopicAndUserAttempts(userID);
        }

        return exerciseSummaryWithTopicDTOList;
    }

    @Override
    public ExerciseDetailsDTO getExerciseDetails(Long id) {
        Optional<Exercise> object = exerciseRepository.findByIDWithTags(id);
        if (object.isEmpty()) {
            throw new IDNotFoundException();
        }

        Exercise entity = object.get();

        return exerciseMapper.exerciseToExerciseDetailsDTO(entity);
    }

    @Override
    public List<ExerciseStatisticDTO> getExerciseStatistics(List<Long> exerciseIDs) {
        List<ExerciseStatisticDTO> exerciseStatisticDTOList = exerciseRepository.getExercisesStatisticDTOs(exerciseIDs);

        return exerciseStatisticDTOList;
    }

    @Override
    public List<ExerciseUserStatisticDTO> getExerciseUserStatistic(List<Long> exerciseIDs, Authentication authentication) {
        UUID userID = SecurityUtils.getUserID(authentication);
        List<ExerciseUserStatisticDTO> exerciseUserStatisticDTOs;

        if (userID == null) {
            exerciseUserStatisticDTOs = exerciseMapper.exerciseIDListToExerciseUserStatisticDTOList(exerciseIDs);
        } else {
            exerciseUserStatisticDTOs = exerciseRepository.getExerciseUserStatisticDTOs(exerciseIDs, userID);
        }

        return exerciseUserStatisticDTOs;
    }

    @Override
    public ExerciseDetailsDTO createExercise(ExerciseRequestDTO dto, Authentication authentication) {
        UUID userID = SecurityUtils.getUserIDOnNullThrowException(authentication);
        Exercise exercise = exerciseMapper.updateExerciseFromExerciseRequestDTO(dto, null, userID);
        exercise = exerciseRepository.save(exercise);

        return exerciseMapper.exerciseToExerciseDetailsDTO(exercise);
    }

    @Override
    public ExerciseDetailsDTO updateExercise(ExerciseRequestDTO dto, Long exerciseID, Authentication authentication) {
        UUID userID = SecurityUtils.getUserIDOnNullThrowException(authentication);
        Optional<Exercise> exerciseOpt = exerciseRepository.findById(exerciseID);
        Validate.isTrue(exerciseOpt.isPresent(), String.format("Exercise with id = %s not found", exerciseID));

        Exercise exerciseEntity = exerciseOpt.get();
        exerciseEntity = exerciseMapper.updateExerciseFromExerciseRequestDTO(dto, exerciseEntity, userID);

        return exerciseMapper.exerciseToExerciseDetailsDTO(exerciseEntity);
    }

    @Override
    public void deleteExercise(Long exerciseID) {
        exerciseRepository.deleteById(exerciseID);
    }

    @Override
    public List<ExerciseAvailableActionDTO> getAvailableExerciseAction(List<Long> exerciseIDs, Authentication authentication) {
        List<ExerciseAvailableActionDTO> exerciseAvailableActionDTOList = new ArrayList<>();

        for (Long exerciseID : exerciseIDs) {
            if (exerciseID == null) {
                continue;
            }

            ExerciseAvailableActionDTO exerciseAvailableActionDTO = new ExerciseAvailableActionDTO();
            exerciseAvailableActionDTO.setId(exerciseID);
            List<String> availableAction = new ArrayList<>();

            if (exercisePermissionEvaluator.hasPermission(authentication, exerciseID, ExerciseActionPermissionRequest.READ_PERMISSION)) {
                availableAction.add(ExerciseActionAvailableConstant.READ_ACTION);
            }
            if (exercisePermissionEvaluator.hasPermission(authentication, exerciseID, ExerciseActionPermissionRequest.UPDATE_PERMISSION)) {
                availableAction.add(ExerciseActionAvailableConstant.UPDATE_ACTION);
            }
            if (exercisePermissionEvaluator.hasPermission(authentication, exerciseID, ExerciseActionPermissionRequest.DELETE_PERMISSION)) {
                availableAction.add(ExerciseActionAvailableConstant.DELETE_ACTION);
            }
            if (exercisePermissionEvaluator.hasPermission(authentication, exerciseID, ExerciseActionPermissionRequest.REPORT_PERMISSION)) {
                availableAction.add(ExerciseActionAvailableConstant.REPORT_ACTION);
            }

            exerciseAvailableActionDTO.setAvailableActions(availableAction);

            exerciseAvailableActionDTOList.add(exerciseAvailableActionDTO);
        }
        return exerciseAvailableActionDTOList;
    }

    @Override
    public void updateAttempts(Long exerciseID) {
        Optional<Exercise> exerciseOpt = exerciseRepository.findById(exerciseID);
        Validate.isTrue(exerciseOpt.isPresent(), "Exercise ID not found. Cannot update.");
        Exercise exercise = exerciseOpt.get();

        Long exerciseAttempts = exerciseAttemptRepository.countByExerciseId(exerciseID);
        exercise.setAttempts(exerciseAttempts);
        exerciseRepository.save(exercise);
    }
}
