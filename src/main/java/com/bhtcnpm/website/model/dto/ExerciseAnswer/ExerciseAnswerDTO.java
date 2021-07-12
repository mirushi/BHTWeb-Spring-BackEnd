package com.bhtcnpm.website.model.dto.ExerciseAnswer;

import lombok.Data;

@Data
public class ExerciseAnswerDTO {
    private Long id;
    private String content;
    private Integer rank;
    private Long questionID;
}
