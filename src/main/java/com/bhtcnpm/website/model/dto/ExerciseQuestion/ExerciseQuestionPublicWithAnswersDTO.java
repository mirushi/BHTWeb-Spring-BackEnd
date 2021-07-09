package com.bhtcnpm.website.model.dto.ExerciseQuestion;

import com.bhtcnpm.website.model.dto.ExerciseAnswer.ExerciseAnswerWithIsCorrectDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class ExerciseQuestionPublicWithAnswersDTO {
    private Long id;
    private String content;
    private Integer rank;
    private String explanation;
    private Long exerciseID;
    private UUID authorID;
    private Integer suggestedDuration;
    private LocalDateTime publishDtm;
    private List<ExerciseAnswerWithIsCorrectDTO> answers;
}
