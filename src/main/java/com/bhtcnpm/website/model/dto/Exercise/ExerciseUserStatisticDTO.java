package com.bhtcnpm.website.model.dto.Exercise;

import lombok.Data;

@Data
public class ExerciseUserStatisticDTO {
    private Long id;
    private Integer bestCorrectQuestions;
    private String note;

    public ExerciseUserStatisticDTO (Long id, Integer bestCorrectQuestions, String note) {
        this.id = id;
        this.bestCorrectQuestions = bestCorrectQuestions;
        this.note = note;
    }
}
