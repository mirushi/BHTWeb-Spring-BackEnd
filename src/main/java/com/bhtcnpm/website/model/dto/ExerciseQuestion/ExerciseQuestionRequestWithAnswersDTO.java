package com.bhtcnpm.website.model.dto.ExerciseQuestion;

import com.bhtcnpm.website.model.dto.ExerciseAnswer.ExerciseAnswerRequestAllDTO;
import com.bhtcnpm.website.model.dto.ExerciseAnswer.ExerciseAnswerRequestContentOnlyDTO;
import com.bhtcnpm.website.model.dto.ExerciseAnswer.ExerciseAnswerRequestWithIDDTO;
import lombok.Data;

import java.util.List;

@Data
public class ExerciseQuestionRequestWithAnswersDTO {
    private String content;
    private Integer rank;
    private String explanation;
    private Long exerciseID;
    private Integer suggestedDuration;
    private List<ExerciseAnswerRequestContentOnlyDTO> exerciseAnswerRequestDTOs;
}
