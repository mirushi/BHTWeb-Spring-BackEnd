package com.bhtcnpm.website.model.dto.ExerciseSubjectUserScore;

import lombok.Data;

import java.util.UUID;

@Data
public class ExerciseSubjectUserScoreDTO {
    private UUID userID;
    private String userDisplayName;
    private String userAvatarURL;
    private Integer rank;
    private Long totalScore;
}
