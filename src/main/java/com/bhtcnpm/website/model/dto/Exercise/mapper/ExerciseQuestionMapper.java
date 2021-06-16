package com.bhtcnpm.website.model.dto.Exercise.mapper;

import com.bhtcnpm.website.model.dto.Exercise.ExerciseQuestionWithAnswersDTO;
import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseQuestion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(uses = {ExerciseAnswerMapper.class})
public interface ExerciseQuestionMapper {
    @Mapping(target = "exerciseAnswerDTOs", source = "answers")
    ExerciseQuestionWithAnswersDTO exerciseQuestionToExerciseQuestionWithAnswersDTO (ExerciseQuestion exerciseQuestion);

    List<ExerciseQuestionWithAnswersDTO> exerciseQuestionListToExerciseQuestionWithAnswersDTOList (List<ExerciseQuestion> exerciseQuestionList);
}
