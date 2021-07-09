package com.bhtcnpm.website.model.dto.ExerciseQuestion.mapper;

import com.bhtcnpm.website.model.dto.ExerciseAnswer.mapper.ExerciseAnswerMapper;
import com.bhtcnpm.website.model.dto.ExerciseQuestion.ExerciseQuestionRequestDTO;
import com.bhtcnpm.website.model.dto.ExerciseQuestion.ExerciseQuestionWithAnswersDTO;
import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseQuestion;
import com.bhtcnpm.website.model.entity.enumeration.ExerciseQuestion.ExerciseQuestionStateType;
import com.bhtcnpm.website.repository.Exercise.ExerciseRepository;
import com.bhtcnpm.website.repository.UserWebsiteRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Mapper(uses = {ExerciseAnswerMapper.class})
public abstract class ExerciseQuestionMapper {

    protected UserWebsiteRepository userWebsiteRepository;
    protected ExerciseRepository exerciseRepository;
    protected ExerciseAnswerMapper exerciseAnswerMapper;

    @Mapping(target = "exerciseAnswerDTOs", source = "answers")
    public abstract ExerciseQuestionWithAnswersDTO exerciseQuestionToExerciseQuestionWithAnswersDTO (ExerciseQuestion exerciseQuestion);

    public abstract List<ExerciseQuestionWithAnswersDTO> exerciseQuestionListToExerciseQuestionWithAnswersDTOList (List<ExerciseQuestion> exerciseQuestionList);

    public ExerciseQuestion updateExerciseQuestionFromExerciseQuestionRequestDTO (ExerciseQuestionRequestDTO dto, ExerciseQuestion entity, UUID userID) {
        ExerciseQuestion newExerciseQuestion = Objects.requireNonNullElseGet(entity, ExerciseQuestion::new);

        if (dto == null) {
            return entity;
        }

        if (entity == null) {
            newExerciseQuestion.setAuthor(userWebsiteRepository.getOne(userID));
            newExerciseQuestion.setSubmitDtm(LocalDateTime.now());
            //TODO: Change this to PENDING once we have GUI for approval.
            newExerciseQuestion.setStateType(ExerciseQuestionStateType.APPROVED);
            newExerciseQuestion.setVersion((short)0);
        }

        newExerciseQuestion.setContent(dto.getContent());
        newExerciseQuestion.setRank(dto.getRank());
        newExerciseQuestion.setExplanation(dto.getExplanation());
        newExerciseQuestion.setExercise(exerciseRepository.getOne(dto.getExerciseID()));
        newExerciseQuestion.setSuggestedDuration(dto.getSuggestedDuration());
        newExerciseQuestion.setLastUpdatedBy(userWebsiteRepository.getOne(userID));
        newExerciseQuestion.setAnswers(exerciseAnswerMapper.exerciseAnswerDTOListToExerciseAnswerList(dto.getExerciseAnswerRequestDTOs()));

        return newExerciseQuestion;
    }

    @Autowired
    public void setUserWebsiteRepository (UserWebsiteRepository userWebsiteRepository) { this.userWebsiteRepository = userWebsiteRepository; }

    @Autowired
    public void setExerciseRepository (ExerciseRepository exerciseRepository) { this.exerciseRepository = exerciseRepository; }

    @Autowired
    public void setExerciseAnswerMapper (ExerciseAnswerMapper exerciseAnswerMapper) { this.exerciseAnswerMapper = exerciseAnswerMapper; }
}
