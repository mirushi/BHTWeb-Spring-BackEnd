package com.bhtcnpm.website.model.dto.ExerciseQuestion;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ExerciseQuestionPublicDTO {
    private Long id;
    private String content;
    private Integer rank;
    private String explanation;
    private Long exerciseID;
    private UUID authorID;
    private Integer suggestedDuration;
    private LocalDateTime publishDtm;
}
