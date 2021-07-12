package com.bhtcnpm.website.model.dto.ExerciseQuestion;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExerciseQuestionRequestDTO {
    private String content;
    private Integer rank;
    private String explanation;
    private Integer suggestedDuration;
    private Integer difficultyID;
    private LocalDateTime publishDtm;
}
