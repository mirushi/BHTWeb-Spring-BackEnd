package com.bhtcnpm.website.model.dto.ExerciseQuestion;

import com.bhtcnpm.website.model.dto.ExerciseAnswer.ExerciseAnswerRequestContentOnlyDTO;
import com.bhtcnpm.website.model.dto.ExerciseAnswer.ExerciseAnswerRequestWithIDDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ExerciseQuestionRequestWithIDAndAnswersWithIDsDTO {
    private Long id;
    private String content;
    private Integer rank;
    private String explanation;
    private Integer suggestedDuration;
    private Integer difficultyID;
    private LocalDateTime publishDtm;
    private List<ExerciseAnswerRequestWithIDDTO> exerciseAnswerRequestDTOs;
}
