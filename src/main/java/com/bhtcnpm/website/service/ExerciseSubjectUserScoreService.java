package com.bhtcnpm.website.service;

import com.bhtcnpm.website.model.dto.ExerciseSubjectUserScore.ExerciseSubjectScoreboardWithUserRankDTO;
import com.bhtcnpm.website.model.dto.ExerciseSubjectUserScore.ExerciseSubjectUserScoreDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import java.util.UUID;

public interface ExerciseSubjectUserScoreService {
    @PreAuthorize(value = "isAuthenticated()")
    boolean addScore (UUID userID, Long subjectID, long score);

    @PreAuthorize(value = "permitAll()")
    ExerciseSubjectScoreboardWithUserRankDTO getScoreboard (Long subjectID, Authentication authentication);

    @PreAuthorize(value = "permitAll()")
    ExerciseSubjectUserScoreDTO getUserScore (UUID userID, Long subjectID);
}
