package com.bhtcnpm.website.controller.Exercise;

import com.bhtcnpm.website.model.dto.ExerciseSubjectUserScore.ExerciseSubjectScoreboardWithUserRankDTO;
import com.bhtcnpm.website.service.ExerciseSubjectUserScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequiredArgsConstructor
public class ExerciseSubjectUserScoreController {

    private final ExerciseSubjectUserScoreService exerciseSubjectUserScoreService;

    @GetMapping("/subjects/{subjectID}/exerciseScoreBoard")
    @ResponseBody
    public ResponseEntity<ExerciseSubjectScoreboardWithUserRankDTO> getExerciseSubjectScoreboard (@PathVariable("subjectID") Long subjectID,
                                                                                                  Authentication authentication) {
        ExerciseSubjectScoreboardWithUserRankDTO scoreboardDTO = exerciseSubjectUserScoreService.getScoreboard(subjectID, authentication);

        return new ResponseEntity<>(scoreboardDTO, HttpStatus.OK);
    }
}
