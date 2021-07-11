package com.bhtcnpm.website.service.Exercise.impl;

import com.bhtcnpm.website.model.dto.ExerciseQuestionDifficulty.ExerciseQuestionDifficultyDTO;
import com.bhtcnpm.website.model.dto.ExerciseQuestionDifficulty.mapper.ExerciseQuestionDifficultyMapper;
import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseQuestionDifficulty;
import com.bhtcnpm.website.repository.Exercise.ExerciseQuestionDifficultyRepository;
import com.bhtcnpm.website.service.Exercise.ExerciseQuestionDifficultyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ExerciseQuestionDifficultyServiceImpl implements ExerciseQuestionDifficultyService {

    private final ExerciseQuestionDifficultyRepository exerciseQuestionDifficultyRepository;
    private final ExerciseQuestionDifficultyMapper exerciseQuestionDifficultyMapper;

    @Override
    public List<ExerciseQuestionDifficultyDTO> getExerciseQuestionDifficultyList() {
        List<ExerciseQuestionDifficulty> difficultyEntityList = exerciseQuestionDifficultyRepository.findAll();

        return exerciseQuestionDifficultyMapper.exerciseQuestionDifficultyListToExerciseQuestionDifficultyDTOList(difficultyEntityList);
    }
}
