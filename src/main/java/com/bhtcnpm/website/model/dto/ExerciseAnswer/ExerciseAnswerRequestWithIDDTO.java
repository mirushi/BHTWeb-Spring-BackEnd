package com.bhtcnpm.website.model.dto.ExerciseAnswer;

import lombok.Data;

@Data
public class ExerciseAnswerRequestWithIDDTO {
    private Long id;
    private String content;
    private Integer rank;
    private Boolean isCorrect;
}
