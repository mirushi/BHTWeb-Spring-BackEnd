package com.bhtcnpm.website.service.Exercise;

import com.bhtcnpm.website.model.dto.ExerciseQuestion.*;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ExerciseQuestionService {
    List<ExerciseQuestionWithAnswersDTO> getExerciseQuestionWithAnswers (Long exerciseID);
    List<ExerciseQuestionResultDTO> submitAttemptAndGetResult (Long exerciseID , List<ExerciseQuestionSubmitDTO> submitDTOs, Authentication authentication);
    ExerciseQuestionPublicDTO createQuestion (Long exerciseID, ExerciseQuestionRequestDTO requestDTO, Authentication authentication);
}
