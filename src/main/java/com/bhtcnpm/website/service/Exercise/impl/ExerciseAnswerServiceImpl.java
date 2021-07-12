package com.bhtcnpm.website.service.Exercise.impl;

import com.bhtcnpm.website.model.dto.ExerciseAnswer.ExerciseAnswerDTO;
import com.bhtcnpm.website.model.dto.ExerciseAnswer.ExerciseAnswerRequestContentOnlyDTO;
import com.bhtcnpm.website.model.dto.ExerciseAnswer.ExerciseAnswerRequestWithIDDTO;
import com.bhtcnpm.website.model.dto.ExerciseAnswer.ExerciseAnswerWithIsCorrectDTO;
import com.bhtcnpm.website.model.dto.ExerciseAnswer.mapper.ExerciseAnswerMapper;
import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseAnswer;
import com.bhtcnpm.website.repository.Exercise.ExerciseAnswerRepository;
import com.bhtcnpm.website.repository.Exercise.ExerciseQuestionRepository;
import com.bhtcnpm.website.service.Exercise.ExerciseAnswerService;
import com.bhtcnpm.website.service.util.PaginatorUtils;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.Validate;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ExerciseAnswerServiceImpl implements ExerciseAnswerService {

    private final int MAX_ANSWER_PER_REQUEST = 10;

    private final ExerciseAnswerRepository exerciseAnswerRepository;
    private final ExerciseQuestionRepository exerciseQuestionRepository;
    private final ExerciseAnswerMapper exerciseAnswerMapper;

    @Override
    public List<ExerciseAnswerDTO> getAnswerWithoutIsCorrect(Long questionID, Authentication authentication) {
        List<ExerciseAnswer> exerciseAnswers = exerciseAnswerRepository.findAllByQuestionId(questionID);

        return exerciseAnswerMapper.exerciseAnswerListToExerciseAnswerDTOList(exerciseAnswers);
    }

    @Override
    public List<ExerciseAnswerWithIsCorrectDTO> getAnswerWithIsCorrect(Predicate predicate, Pageable pageable, Authentication authentication) {
        pageable = PaginatorUtils.getPageableWithNewPageSize(pageable, MAX_ANSWER_PER_REQUEST);
        Iterable<ExerciseAnswer> exerciseAnswers = exerciseAnswerRepository.findAll(predicate, pageable);

        return exerciseAnswerMapper.exerciseAnswerListToExerciseAnswerWithIsCorrectDTOList(exerciseAnswers);
    }

    @Override
    public ExerciseAnswerWithIsCorrectDTO createAnswer(ExerciseAnswerRequestContentOnlyDTO requestDTO, Long questionID, Authentication authentication) {
        ExerciseAnswer exerciseAnswer = exerciseAnswerMapper.createNewExerciseAnswerFromExerciseAnswerRequestContentOnlyDTO(requestDTO, questionID);

        exerciseAnswer = exerciseAnswerRepository.save(exerciseAnswer);

        return exerciseAnswerMapper.exerciseAnswerToExerciseAnswerWithIsCorrectDTO(exerciseAnswer);
    }

    @Override
    public List<ExerciseAnswerWithIsCorrectDTO> createMultipleAnswers (List<ExerciseAnswerRequestContentOnlyDTO> requestDTOs, Long questionID, Authentication authentication) {
        List<ExerciseAnswer> exerciseAnswers = requestDTOs.stream()
                .map(obj -> exerciseAnswerMapper.createNewExerciseAnswerFromExerciseAnswerRequestContentOnlyDTO(obj, questionID))
                .collect(Collectors.toList());

        exerciseAnswers = exerciseAnswerRepository.saveAll(exerciseAnswers);

        return exerciseAnswerMapper.exerciseAnswerListToExerciseAnswerWithIsCorrectDTOList(exerciseAnswers);
    }

    @Override
    public ExerciseAnswerWithIsCorrectDTO updateAnswer(ExerciseAnswerRequestContentOnlyDTO requestDTO, Long answerID, Authentication authentication) {
        Optional<ExerciseAnswer> exerciseAnswer = exerciseAnswerRepository.findById(answerID);
        Validate.isTrue(exerciseAnswer.isPresent(), "Exercise Answer with id = %s does not exists.", answerID);

        ExerciseAnswer entity = exerciseAnswer.get();
        entity = exerciseAnswerMapper.updateExerciseAnswerFromExerciseAnswerRequestDTO(requestDTO, entity);

        entity = exerciseAnswerRepository.save(entity);

        return exerciseAnswerMapper.exerciseAnswerToExerciseAnswerWithIsCorrectDTO(entity);
    }

    @Override
    public List<ExerciseAnswerWithIsCorrectDTO> updateMultipleAnswers (List<ExerciseAnswerRequestWithIDDTO> requestDTOList, Authentication authentication) {
        List<ExerciseAnswer> exerciseAnswerList = exerciseAnswerRepository.findAllById(requestDTOList.stream().map(ExerciseAnswerRequestWithIDDTO::getId).collect(Collectors.toList()));
        Validate.isTrue(exerciseAnswerList.size() == requestDTOList.size(), "Some answers cannot be found.");

        exerciseAnswerList = exerciseAnswerMapper.updateExerciseAnswerWithExerciseAnswerRequestWithIDDTOList(requestDTOList, exerciseAnswerList);

        exerciseAnswerList = exerciseAnswerRepository.saveAll(exerciseAnswerList);

        return exerciseAnswerMapper.exerciseAnswerListToExerciseAnswerWithIsCorrectDTOList(exerciseAnswerList);
    }

    @Override
    public void deleteAnswer(Long answerID, Authentication authentication) {
        exerciseAnswerRepository.deleteById(answerID);
    }
}
