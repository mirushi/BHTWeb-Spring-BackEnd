package com.bhtcnpm.website.model.dto.ExerciseQuestion;

import com.bhtcnpm.website.model.dto.ExerciseAnswer.ExerciseAnswerWithIsCorrectDTO;
import com.bhtcnpm.website.model.dto.ExerciseQuestionDifficulty.ExerciseQuestionDifficultyDTO;
import com.bhtcnpm.website.model.dto.UserWebsite.UserSummaryDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class ExerciseQuestionWithAnswersDTO {
    private Long id;
    private String content;
    private Integer rank;
    private String explanation;
    private Long exerciseID;
    private UserSummaryDTO author;
    private Integer suggestedDuration;
    private LocalDateTime publishDtm;
    private LocalDateTime lastUpdatedDtm;
    private UserSummaryDTO lastUpdatedBy;
    private ExerciseQuestionDifficultyDTO difficultyType;
    private List<ExerciseAnswerWithIsCorrectDTO> answers;
}
