package com.bhtcnpm.website.service.Exercise.impl;

import com.bhtcnpm.website.model.dto.Exercise.ExerciseDetailsDTO;
import com.bhtcnpm.website.model.dto.Exercise.ExerciseStatisticDTO;
import com.bhtcnpm.website.model.dto.Exercise.ExerciseSummaryDTO;
import com.bhtcnpm.website.model.dto.Exercise.ExerciseSummaryWithTopicDTO;
import com.bhtcnpm.website.model.dto.Exercise.mapper.ExerciseMapper;
import com.bhtcnpm.website.model.entity.ExerciseEntities.Exercise;
import com.bhtcnpm.website.model.exception.IDNotFoundException;
import com.bhtcnpm.website.repository.Exercise.ExerciseRepository;
import com.bhtcnpm.website.security.predicate.Exercise.ExerciseOrderingGenerator;
import com.bhtcnpm.website.security.predicate.Exercise.ExercisePredicateGenerator;
import com.bhtcnpm.website.security.util.SecurityUtils;
import com.bhtcnpm.website.service.Exercise.ExerciseService;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ExerciseServiceImpl implements ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final ExerciseMapper exerciseMapper;

    @Override
    public List<ExerciseSummaryDTO> getExerciseList(Predicate predicate, Authentication authentication) {
        UUID userID = SecurityUtils.getUserID(authentication);
        Sort sortByRankAsc = ExerciseOrderingGenerator.orderByRankAsc();

        List<ExerciseSummaryDTO> exerciseSummaryDTOList;

        if (userID == null) {
            Iterable<Exercise> exerciseIterable = exerciseRepository.findAll(predicate, sortByRankAsc);
            exerciseSummaryDTOList = exerciseMapper.exerciseIterableToExerciseSummaryDTOList(exerciseIterable);
        } else {
            BooleanExpression userAttempt = ExercisePredicateGenerator.getBooleanExpressionExerciseUserAttempt(userID);
            exerciseSummaryDTOList = exerciseRepository.getExerciseSummaryWithUserAttempts(userAttempt.and(predicate));
        }

        return exerciseSummaryDTOList;
    }

    @Override
    public List<ExerciseSummaryWithTopicDTO> getExerciseWithTopic(Predicate predicate, Authentication authentication) {
        UUID userID = SecurityUtils.getUserID(authentication);
        Sort sortByRankAsc = ExerciseOrderingGenerator.orderByRankAsc();

        List<ExerciseSummaryWithTopicDTO> exerciseSummaryWithTopicDTOList;

        if (userID == null) {
            Iterable<Exercise> exerciseIterable = exerciseRepository.findAll(predicate, sortByRankAsc);
            exerciseSummaryWithTopicDTOList = exerciseMapper.exerciseIterableToExerciseSummaryWithTopicDTOList(exerciseIterable);
        } else {
            BooleanExpression userAttempt = ExercisePredicateGenerator.getBooleanExpressionExerciseUserAttempt(userID);
            exerciseSummaryWithTopicDTOList = exerciseRepository.getExerciseSummaryWithTopicAndUserAttempts(userAttempt.and(predicate));
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
}
