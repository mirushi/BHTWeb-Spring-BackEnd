package com.bhtcnpm.website.model.dto.Exercise.mapper;

import com.bhtcnpm.website.model.dto.Exercise.ExerciseAnswerDTO;
import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseAnswer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface ExerciseAnswerMapper {
    @Mapping(target = "questionID", source = "question.id")
    ExerciseAnswerDTO exerciseAnswerToExerciseAnswerDTO (ExerciseAnswer exerciseAnswer);

    List<ExerciseAnswerDTO> exerciseAnswerListToExerciseAnswerDTOList (List<ExerciseAnswer> exerciseAnswerList);
}
