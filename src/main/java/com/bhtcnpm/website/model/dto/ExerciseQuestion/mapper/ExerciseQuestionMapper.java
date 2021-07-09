package com.bhtcnpm.website.model.dto.ExerciseQuestion.mapper;

import com.bhtcnpm.website.model.dto.ExerciseAnswer.mapper.ExerciseAnswerMapper;
import com.bhtcnpm.website.model.dto.ExerciseQuestion.*;
import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseQuestion;
import com.bhtcnpm.website.model.entity.enumeration.ExerciseQuestion.ExerciseQuestionStateType;
import com.bhtcnpm.website.repository.Exercise.ExerciseRepository;
import com.bhtcnpm.website.repository.UserWebsiteRepository;
import com.bhtcnpm.website.util.DtmUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(uses = {ExerciseAnswerMapper.class})
public abstract class ExerciseQuestionMapper {

    protected UserWebsiteRepository userWebsiteRepository;
    protected ExerciseRepository exerciseRepository;
    protected ExerciseAnswerMapper exerciseAnswerMapper;

    @Mapping(target = "exerciseAnswerDTOs", source = "answers")
    public abstract ExerciseQuestionWithAnswersDTO exerciseQuestionToExerciseQuestionWithAnswersDTO (ExerciseQuestion exerciseQuestion);

    public abstract List<ExerciseQuestionWithAnswersDTO> exerciseQuestionListToExerciseQuestionWithAnswersDTOList (List<ExerciseQuestion> exerciseQuestionList);

    @Mapping(target = "authorID", source = "author.id")
    @Mapping(target = "exerciseID", source = "exercise.id")
    public abstract ExerciseQuestionPublicDTO exerciseQuestionToExerciseQuestionPublicDTO (ExerciseQuestion exerciseQuestion);

    public abstract List<ExerciseQuestionPublicDTO> exerciseQuestionListToExerciseQuestionPublicDTOList (List<ExerciseQuestion> exerciseQuestionsList);

    @Mapping(target = "authorID", source = "author.id")
    @Mapping(target = "exerciseID", source = "exercise.id")
    public abstract ExerciseQuestionPublicWithAnswersDTO exerciseQuestionToExerciseQuestionPublicWithAnswersDTO (ExerciseQuestion exerciseQuestion);

    public abstract List<ExerciseQuestionPublicWithAnswersDTO> exerciseQuestionListToExerciseQuestionPublicWithAnswersDTOList (List<ExerciseQuestion> exerciseQuestionList);

    public ExerciseQuestion exerciseQuestionRequestDTOToExerciseQuestion (ExerciseQuestionRequestDTO requestDTO, Long exerciseID, UUID userID) {
        return ExerciseQuestion.builder()
                .content(requestDTO.getContent())
                .rank(requestDTO.getRank())
                .explanation(requestDTO.getExplanation())
                .exercise(exerciseRepository.getOne(exerciseID))
                .author(userWebsiteRepository.getOne(userID))
                .suggestedDuration(requestDTO.getSuggestedDuration())
                .publishDtm(DtmUtils.checkAndGetPublishDtm(requestDTO.getPublishDtm()))
                .lastUpdatedBy(userWebsiteRepository.getOne(userID))
                //TODO: Change this to PENDING when approval system is done.
                .stateType(ExerciseQuestionStateType.APPROVED)
                .version((short)0)
                .build();
    }

    public List<ExerciseQuestion> exerciseQuestionRequestDTOListToExerciseQuestionList (List<ExerciseQuestionRequestDTO> requestDTOList, Long exerciseID, UUID userID) {
        return requestDTOList.stream()
                .map(obj -> this.exerciseQuestionRequestDTOToExerciseQuestion(obj, exerciseID, userID))
                .collect(Collectors.toList());
    }

    public ExerciseQuestion exerciseQuestionRequestWithAnswersDTOToExerciseQuestion (ExerciseQuestionRequestWithAnswersDTO requestDTO, Long exerciseID, UUID userID) {
        ExerciseQuestion result = new ExerciseQuestion();

        result.setContent(requestDTO.getContent());
        result.setRank(requestDTO.getRank());
        result.setExplanation(requestDTO.getExplanation());
        result.setExercise(exerciseRepository.getOne(exerciseID));
        result.setAuthor(userWebsiteRepository.getOne(userID));
        result.setSuggestedDuration(requestDTO.getSuggestedDuration());
        result.setPublishDtm(DtmUtils.checkAndGetPublishDtm(requestDTO.getPublishDtm()));
        result.setLastUpdatedBy(userWebsiteRepository.getOne(userID));
        //TODO: Change this to PENDING when approval system is done.
        result.setStateType(ExerciseQuestionStateType.APPROVED);
        result.setAnswers(exerciseAnswerMapper
                        .createNewExerciseAnswerListFromExerciseAnswerRequestContentOnlyDTOList(requestDTO.getExerciseAnswerRequestDTOs(), result));
        result.setVersion((short)0);

        return result;
    }

    public List<ExerciseQuestion> exerciseQuestionRequestWithAnswersDTOListToExerciseQuestionList (List<ExerciseQuestionRequestWithAnswersDTO> requestDTOList, Long exerciseID, UUID userID) {
        return requestDTOList.stream()
                .map(obj -> this.exerciseQuestionRequestWithAnswersDTOToExerciseQuestion(obj, exerciseID, userID))
                .collect(Collectors.toList());
    }

//    public ExerciseQuestion updateExerciseQuestionFromExerciseQuestionRequestDTO (ExerciseQuestionRequestWithAnswersDTO dto, ExerciseQuestion entity, UUID userID) {
//        ExerciseQuestion newExerciseQuestion = Objects.requireNonNullElseGet(entity, ExerciseQuestion::new);
//
//        if (dto == null) {
//            return entity;
//        }
//
//        if (entity == null) {
//            newExerciseQuestion.setAuthor(userWebsiteRepository.getOne(userID));
//            newExerciseQuestion.setSubmitDtm(LocalDateTime.now());
//            //TODO: Change this to PENDING once we have GUI for approval.
//            newExerciseQuestion.setStateType(ExerciseQuestionStateType.APPROVED);
//            newExerciseQuestion.setVersion((short)0);
//        }
//
//        newExerciseQuestion.setContent(dto.getContent());
//        newExerciseQuestion.setRank(dto.getRank());
//        newExerciseQuestion.setExplanation(dto.getExplanation());
//        newExerciseQuestion.setExercise(exerciseRepository.getOne(dto.getExerciseID()));
//        newExerciseQuestion.setSuggestedDuration(dto.getSuggestedDuration());
//        newExerciseQuestion.setLastUpdatedBy(userWebsiteRepository.getOne(userID));
//        newExerciseQuestion.setAnswers(exerciseAnswerMapper.exerciseAnswerDTOListToExerciseAnswerList(dto.getExerciseAnswerRequestDTOs()));
//
//        return newExerciseQuestion;
//    }



    @Autowired
    public void setUserWebsiteRepository (UserWebsiteRepository userWebsiteRepository) { this.userWebsiteRepository = userWebsiteRepository; }

    @Autowired
    public void setExerciseRepository (ExerciseRepository exerciseRepository) { this.exerciseRepository = exerciseRepository; }

    @Autowired
    public void setExerciseAnswerMapper (ExerciseAnswerMapper exerciseAnswerMapper) { this.exerciseAnswerMapper = exerciseAnswerMapper; }
}
