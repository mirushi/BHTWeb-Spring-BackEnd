package com.bhtcnpm.website.model.dto.Exercise;

import lombok.Data;

@Data
public class ExerciseUserStatisticDTO {
    private Long id;
    private Integer bestCorrectQuestions;
    private String notes;

    public ExerciseUserStatisticDTO (Long id, Integer bestCorrectQuestions, String notes) {
        this.id = id;
        this.bestCorrectQuestions = bestCorrectQuestions;
        this.notes = notes;
    }
}
