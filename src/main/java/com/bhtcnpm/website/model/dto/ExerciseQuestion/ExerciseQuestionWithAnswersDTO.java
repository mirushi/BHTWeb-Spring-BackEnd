package com.bhtcnpm.website.model.dto.ExerciseQuestion;

import com.bhtcnpm.website.model.dto.ExerciseAnswer.ExerciseAnswerDTO;
import lombok.Data;

import java.util.List;

@Data
public class ExerciseQuestionWithAnswersDTO {
    private Long id;
    private String content;
    private Integer rank;
    private List<ExerciseAnswerDTO> exerciseAnswerDTOs;
}
