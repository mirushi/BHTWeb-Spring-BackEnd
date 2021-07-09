package com.bhtcnpm.website.model.dto.ExerciseAnswer.mapper;

import com.bhtcnpm.website.model.dto.ExerciseAnswer.ExerciseAnswerDTO;
import com.bhtcnpm.website.model.dto.ExerciseAnswer.ExerciseAnswerRequestAllDTO;
import com.bhtcnpm.website.model.dto.ExerciseAnswer.ExerciseAnswerRequestContentOnlyDTO;
import com.bhtcnpm.website.model.dto.ExerciseAnswer.ExerciseAnswerWithIsCorrectDTO;
import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseAnswer;
import com.bhtcnpm.website.repository.Exercise.ExerciseQuestionRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;

@Mapper
public abstract class ExerciseAnswerMapper {

    protected ExerciseQuestionRepository exerciseQuestionRepository;

    @Mapping(target = "questionID", source = "question.id")
    public abstract ExerciseAnswerDTO exerciseAnswerToExerciseAnswerDTO (ExerciseAnswer exerciseAnswer);

    public abstract List<ExerciseAnswerDTO> exerciseAnswerListToExerciseAnswerDTOList (Iterable<ExerciseAnswer> exerciseAnswerList);

    @Mapping(target = "questionID", source = "question.id")
    public abstract ExerciseAnswerWithIsCorrectDTO exerciseAnswerToExerciseAnswerWithIsCorrectDTO (ExerciseAnswer exerciseAnswer);

    public abstract List<ExerciseAnswerWithIsCorrectDTO> exerciseAnswerListToExerciseAnswerWithIsCorrectDTOList (Iterable<ExerciseAnswer> exerciseAnswerList);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "question", ignore = true)
    @Mapping(target = "version", ignore = true)
    public abstract ExerciseAnswer updateExerciseAnswerFromExerciseAnswerRequestDTO (ExerciseAnswerRequestContentOnlyDTO dto ,@MappingTarget ExerciseAnswer entity);

    public ExerciseAnswer exerciseAnswerDTOToExerciseAnswer (ExerciseAnswerRequestAllDTO exerciseAnswerDTO) {
        return ExerciseAnswer.builder()
                .id(exerciseAnswerDTO.getId())
                .content(exerciseAnswerDTO.getContent())
                .isCorrect(exerciseAnswerDTO.getIsCorrect())
                .rank(exerciseAnswerDTO.getRank())
                .question(exerciseQuestionRepository.getOne(exerciseAnswerDTO.getQuestionID()))
                .version((short)0)
                .build();
    }

    public abstract List<ExerciseAnswer> exerciseAnswerDTOListToExerciseAnswerList (List<ExerciseAnswerRequestAllDTO> dto);

    @Autowired
    public void setExerciseQuestionRepository (ExerciseQuestionRepository exerciseQuestionRepository) { this.exerciseQuestionRepository = exerciseQuestionRepository; }

}
