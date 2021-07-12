package com.bhtcnpm.website.model.dto.ExerciseQuestion;

import lombok.Data;

import java.util.List;

@Data
public class ExerciseQuestionResultDTO {
    private Long id;
    private Boolean isCorrect;
    private Boolean isAnswered;
    private List<Long> answersSelected;
    private List<Long> correctAnswers;
    private String explanation;
}
