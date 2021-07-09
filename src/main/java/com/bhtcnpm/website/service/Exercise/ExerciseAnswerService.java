package com.bhtcnpm.website.service.Exercise;

import com.bhtcnpm.website.model.dto.ExerciseAnswer.ExerciseAnswerDTO;
import com.bhtcnpm.website.model.dto.ExerciseAnswer.ExerciseAnswerRequestContentOnlyDTO;
import com.bhtcnpm.website.model.dto.ExerciseAnswer.ExerciseAnswerRequestWithIDDTO;
import com.bhtcnpm.website.model.dto.ExerciseAnswer.ExerciseAnswerWithIsCorrectDTO;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ExerciseAnswerService {
    List<ExerciseAnswerDTO> getAnswerWithoutIsCorrect (Long questionID, Authentication authentication);
    List<ExerciseAnswerWithIsCorrectDTO> getAnswerWithIsCorrect (Predicate predicate, Pageable pageable, Authentication authentication);
    ExerciseAnswerWithIsCorrectDTO createAnswer (ExerciseAnswerRequestContentOnlyDTO requestDTO, Long questionID, Authentication authentication);
    List<ExerciseAnswerWithIsCorrectDTO> createMultipleAnswers (List<ExerciseAnswerRequestContentOnlyDTO> requestDTOs, Long questionID, Authentication authentication);
    ExerciseAnswerWithIsCorrectDTO updateAnswer (ExerciseAnswerRequestContentOnlyDTO requestDTO, Long answerID, Authentication authentication);
    List<ExerciseAnswerWithIsCorrectDTO> updateMultipleAnswers (List<ExerciseAnswerRequestWithIDDTO> requestDTOList, Authentication authentication);
    void deleteAnswer (Long answerID, Authentication authentication);
}
