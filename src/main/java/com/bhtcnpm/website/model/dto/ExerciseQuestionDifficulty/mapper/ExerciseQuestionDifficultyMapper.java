package com.bhtcnpm.website.model.dto.ExerciseQuestionDifficulty.mapper;

import com.bhtcnpm.website.model.dto.ExerciseQuestionDifficulty.ExerciseQuestionDifficultyDTO;
import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseQuestionDifficulty;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface ExerciseQuestionDifficultyMapper {
    ExerciseQuestionDifficultyDTO exerciseQuestionDifficultyToExerciseQuestionDifficultyDTO (ExerciseQuestionDifficulty entity);
    List<ExerciseQuestionDifficultyDTO> exerciseQuestionDifficultyListToExerciseQuestionDifficultyDTOList (List<ExerciseQuestionDifficulty> entityList);
}
