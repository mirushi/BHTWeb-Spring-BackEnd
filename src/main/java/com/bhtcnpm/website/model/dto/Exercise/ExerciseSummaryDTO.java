package com.bhtcnpm.website.model.dto.Exercise;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseSummaryDTO {
    private Long id;
    private String title;
    private String description;
    private Boolean attempted;
    private Integer maxCorrectAnsweredQuestions;
    private Long totalQuestions;
}
