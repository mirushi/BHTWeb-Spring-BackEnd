package com.bhtcnpm.website.model.dto.Exercise;

import lombok.Data;

import java.util.List;

@Data
public class ExerciseQuestionWithAnswers {
    private Long id;
    private String content;
    private Integer rank;
    private List<ExerciseAnswerDTO> exerciseAnswerDTOs;
}
