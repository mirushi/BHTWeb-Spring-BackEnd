package com.bhtcnpm.website.model.dto.ExerciseQuestion;

import com.bhtcnpm.website.model.dto.ExerciseAnswer.ExerciseAnswerRequestContentOnlyDTO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExerciseQuestionRequestWithIDContentOnlyDTO {
    private Long id;
    private String content;
    private Integer rank;
    private String explanation;
    private Integer suggestedDuration;
    private Integer difficultyID;
    private LocalDateTime publishDtm;
}
