package com.bhtcnpm.website.model.dto.ExerciseQuestion;

import com.bhtcnpm.website.model.dto.ExerciseAnswer.ExerciseAnswerRequestAllDTO;
import lombok.Data;

import java.util.List;

@Data
public class ExerciseQuestionRequestDTO {
    private String content;
    private Integer rank;
    private String explanation;
    private Long exerciseID;
    private Integer suggestedDuration;
    private List<ExerciseAnswerRequestAllDTO> exerciseAnswerRequestDTOs;
}
