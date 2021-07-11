package com.bhtcnpm.website.model.dto.ExerciseAnswer.mapper;

import com.bhtcnpm.website.model.dto.ExerciseAnswer.*;
import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseAnswer;
import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseQuestion;
import com.bhtcnpm.website.repository.Exercise.ExerciseQuestionRepository;
import org.apache.commons.lang3.Validate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class ExerciseAnswerMapper {

    protected ExerciseQuestionRepository exerciseQuestionRepository;

    @Mapping(target = "questionID", source = "question.id")
    public abstract ExerciseAnswerDTO exerciseAnswerToExerciseAnswerDTO (ExerciseAnswer exerciseAnswer);

    public abstract List<ExerciseAnswerDTO> exerciseAnswerListToExerciseAnswerDTOList (Iterable<ExerciseAnswer> exerciseAnswerList);

    @Mapping(target = "questionID", source = "question.id")
    public abstract ExerciseAnswerWithIsCorrectDTO exerciseAnswerToExerciseAnswerWithIsCorrectDTO (ExerciseAnswer exerciseAnswer);

    public abstract List<ExerciseAnswerWithIsCorrectDTO> exerciseAnswerListToExerciseAnswerWithIsCorrectDTOList (Iterable<ExerciseAnswer> exerciseAnswerList);

    public abstract ExerciseAnswerRequestContentOnlyDTO answerRequestWithIDDTOToAnswerRequestContentOnlyDTO(ExerciseAnswerRequestWithIDDTO dto);

    public abstract List<ExerciseAnswerRequestContentOnlyDTO> answerRequestWithIDDTOListToAnswerRequestContentOnlyDTOList (List<ExerciseAnswerRequestWithIDDTO> dtoList);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "question", ignore = true)
    @Mapping(target = "version", ignore = true)
    public abstract ExerciseAnswer updateExerciseAnswerFromExerciseAnswerRequestDTO (ExerciseAnswerRequestContentOnlyDTO dto ,@MappingTarget ExerciseAnswer entity);

    public ExerciseAnswer createNewExerciseAnswerFromExerciseAnswerRequestContentOnlyDTO(ExerciseAnswerRequestContentOnlyDTO requestContentOnlyDTO, Long questionID) {
        return this.createNewExerciseAnswerFromExerciseAnswerRequestContentOnlyDTO(requestContentOnlyDTO, exerciseQuestionRepository.getOne(questionID));
    }

    public ExerciseAnswer createNewExerciseAnswerFromExerciseAnswerRequestContentOnlyDTO(ExerciseAnswerRequestContentOnlyDTO requestContentOnlyDTO, ExerciseQuestion questionEntity) {
        return ExerciseAnswer.builder()
                .id(null)
                .content(requestContentOnlyDTO.getContent())
                .isCorrect(requestContentOnlyDTO.getIsCorrect())
                .rank(requestContentOnlyDTO.getRank())
                .question(questionEntity)
                .version((short)0)
                .build();
    }

    public List<ExerciseAnswer> createNewExerciseAnswerListFromExerciseAnswerRequestContentOnlyDTOList (List<ExerciseAnswerRequestContentOnlyDTO> requestDTOList, ExerciseQuestion questionEntity) {
        return requestDTOList.stream()
                .map(obj -> this.createNewExerciseAnswerFromExerciseAnswerRequestContentOnlyDTO(obj, questionEntity))
                .collect(Collectors.toList());
    }

    public List<ExerciseAnswer> createNewExerciseAnswerListFromExerciseAnswerRequestContentOnlyDTOList (List<ExerciseAnswerRequestContentOnlyDTO> requestDTOList, Long questionID) {
        return this.createNewExerciseAnswerListFromExerciseAnswerRequestContentOnlyDTOList(requestDTOList, exerciseQuestionRepository.getOne(questionID));
    }

    public ExerciseAnswer exerciseAnswerRequestWithIDToExerciseAnswer (ExerciseAnswerRequestWithIDDTO requestDTO, ExerciseAnswer entity) {
        Validate.notNull(entity, "Cannot update null entity in exerciseAnswerRequestWithIDToExerciseAnswer.");

        entity.setIsCorrect(requestDTO.getIsCorrect());
        entity.setContent(requestDTO.getContent());
        entity.setRank(requestDTO.getRank());

        return entity;
    }

    public List<ExerciseAnswer> updateExerciseAnswerWithExerciseAnswerRequestWithIDDTOList (List<ExerciseAnswerRequestWithIDDTO> requestDTOList, List<ExerciseAnswer> entityList) {
        List<ExerciseAnswer> resultList = new ArrayList<>(entityList.size());
        Map<Long, ExerciseAnswerRequestWithIDDTO> exerciseAnswerRequestWithIDDTOMap = new HashMap<>(requestDTOList.size());

        for (ExerciseAnswerRequestWithIDDTO dto : requestDTOList) {
            exerciseAnswerRequestWithIDDTOMap.put(dto.getId(), dto);
        }

        for (ExerciseAnswer entity : entityList) {
            ExerciseAnswerRequestWithIDDTO dto = exerciseAnswerRequestWithIDDTOMap.get(entity.getId());
            if (dto == null) {
                continue;
            }
            ExerciseAnswer answer = this.exerciseAnswerRequestWithIDToExerciseAnswer(dto, entity);
            resultList.add(answer);
        }

        return resultList;
    }


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

    @Mapping(target = "question", expression = "java(exerciseQuestionRepository.getOne(questionID))")
    @Mapping(target = "version", ignore = true)
    public abstract ExerciseAnswer exerciseAnswerRequestWithIDDTOToExerciseAnswer (ExerciseAnswerRequestWithIDDTO dto, Long questionID);

    public List<ExerciseAnswer> exerciseAnswerRequestWithIDDTOListToExerciseAnswerList (List<ExerciseAnswerRequestWithIDDTO> dtoList, Long questionID) {
        return dtoList.stream()
                .map(obj -> this.exerciseAnswerRequestWithIDDTOToExerciseAnswer(obj, questionID))
                .collect(Collectors.toList());
    }

    @Mapping(target = "question", expression = "java((entity.getQuestion() != null) ? (entity.getQuestion()) : (exerciseQuestionRepository.getOne(questionID)))")
    @Mapping(target = "version", ignore = true)
    public abstract ExerciseAnswer updateExerciseAnswerWithExerciseAnswerRequestWithIDDTO (ExerciseAnswerRequestWithIDDTO dto, Long questionID, @MappingTarget ExerciseAnswer entity);

    public List<ExerciseAnswer> updateExerciseAnswerListWithExerciseAnswerRequestWithIDDTOList (List<ExerciseAnswerRequestWithIDDTO> dtoList, Long questionID, List<ExerciseAnswer> entityList) {
        List<ExerciseAnswer> finalList = new ArrayList<>();
        Map<Long, ExerciseAnswer> entityMap = entityList.stream().collect(Collectors.toMap(ExerciseAnswer::getId, Function.identity()));

        for (ExerciseAnswerRequestWithIDDTO dto : dtoList) {
            ExerciseAnswer entity;
            if (dto.getId() != null) {
                entity = entityMap.get(dto.getId());
                Validate.notNull(entity, String.format("Answer with ID = %s not found. Cannot update, did you mean create ?", dto.getId()));
            } else {
                entity = new ExerciseAnswer();
            }

            entity = this.updateExerciseAnswerWithExerciseAnswerRequestWithIDDTO(dto, questionID, entity);

            finalList.add(entity);
        }

        return finalList;
    }

    @Autowired
    public void setExerciseQuestionRepository (ExerciseQuestionRepository exerciseQuestionRepository) { this.exerciseQuestionRepository = exerciseQuestionRepository; }

}
