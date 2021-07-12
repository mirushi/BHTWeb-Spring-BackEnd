package com.bhtcnpm.website.service.Exercise;

import com.bhtcnpm.website.model.dto.ExerciseQuestion.*;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ExerciseQuestionService {
    List<ExerciseQuestionWithAnswersPublicDTO> getPublicExerciseQuestionWithAnswers(Long exerciseID);
    List<ExerciseQuestionWithAnswersDTO> getExerciseQuestionsWithAnswers (Long exerciseID);
    List<ExerciseQuestionResultDTO> submitAttemptAndGetResult (Long exerciseID , List<ExerciseQuestionSubmitDTO> submitDTOs, Authentication authentication);
    ExerciseQuestionDTO createQuestion (Long exerciseID, ExerciseQuestionRequestDTO requestDTO, Authentication authentication);
    List<ExerciseQuestionDTO> createMultipleQuestions (Long exerciseID, List<ExerciseQuestionRequestDTO> requestDTOList, Authentication authentication);
    ExerciseQuestionWithAnswersDTO createQuestionWithAnswers (Long exerciseID, ExerciseQuestionRequestWithAnswersDTO requestDTO, Authentication authentication);
    List<ExerciseQuestionWithAnswersDTO> createMultipleQuestionsWithAnswers (Long exerciseID, List<ExerciseQuestionRequestWithAnswersDTO> requestDTOList, Authentication authentication);
    List<ExerciseQuestionDTO> updateMultipleQuestions (List<ExerciseQuestionRequestWithIDContentOnlyDTO> requestDTOList, Long exerciseID, Authentication authentication);
    List<ExerciseQuestionWithAnswersDTO> updateMultipleQuestionsWithAnswers (List<ExerciseQuestionRequestWithIDAndAnswersWithIDsDTO> requestDTOList, Long exerciseID , Authentication authentication);
    void deleteQuestion (Long questionID, Authentication authentication);
    void deleteMultipleQuestions (List<Long> questionIDs, Authentication authentication);
}
