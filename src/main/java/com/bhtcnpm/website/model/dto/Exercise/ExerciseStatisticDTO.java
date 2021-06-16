package com.bhtcnpm.website.model.dto.Exercise;

import lombok.Data;

@Data
public class ExerciseStatisticDTO {
    private Long id;
    private Long totalQuestions;
    private Long attemptCount;
    public ExerciseStatisticDTO (Long id, Long totalQuestions, Long attemptCount) {
        this.id = id;
        this.totalQuestions = totalQuestions;
        this.attemptCount = attemptCount;
    }
}
