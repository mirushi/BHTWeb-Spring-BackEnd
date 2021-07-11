package com.bhtcnpm.website.model.dto.Exercise;

import lombok.Data;

@Data
public class ExerciseSearchResultDTO {
    private Long id;
    private String title;
    private String description;
    private Boolean attempted;
    private Integer maxCorrectAnsweredQuestions;
    private Long totalQuestions;
}
