package com.bhtcnpm.website.model.dto.Exercise;

import lombok.Data;

@Data
public class ExerciseUserStatisticDTO {
    private Long id;
    private Integer bestCorrectQuestions;
    private String notes;
}
