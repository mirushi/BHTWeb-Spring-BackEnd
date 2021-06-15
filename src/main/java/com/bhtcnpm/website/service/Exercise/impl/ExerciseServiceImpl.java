package com.bhtcnpm.website.service.Exercise.impl;

import com.bhtcnpm.website.model.dto.Exercise.ExerciseSummaryDTO;
import com.bhtcnpm.website.model.dto.Exercise.mapper.ExerciseMapper;
import com.bhtcnpm.website.model.entity.ExerciseEntities.Exercise;
import com.bhtcnpm.website.repository.Exercise.ExerciseRepository;
import com.bhtcnpm.website.security.predicate.Exercise.ExercisePredicateGenerator;
import com.bhtcnpm.website.security.util.SecurityUtils;
import com.bhtcnpm.website.service.Exercise.ExerciseService;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

        List<ExerciseSummaryDTO> exerciseSummaryDTOList;

        if (userID == null) {
            Iterable<Exercise> exerciseIterable = exerciseRepository.findAll(predicate);
            exerciseSummaryDTOList = exerciseMapper.exerciseIterableToExerciseSummaryDTOList(exerciseIterable);
        } else {
            BooleanExpression userAttempt = ExercisePredicateGenerator.getBooleanExpressionExerciseUserAttempt(userID);
            exerciseSummaryDTOList = exerciseRepository.getExerciseSummaryWithUserAttempt(userAttempt.and(predicate));
        }

        return exerciseSummaryDTOList;
    }
}
