package com.bhtcnpm.website.model.dto.ExerciseSubjectUserScore;

import lombok.Data;

import java.util.List;

@Data
public class ExerciseSubjectScoreboardWithUserRankDTO {
    private List<ExerciseSubjectUserScoreDTO> exerciseScoreboard;
    private ExerciseSubjectUserScoreDTO userRank;
}
