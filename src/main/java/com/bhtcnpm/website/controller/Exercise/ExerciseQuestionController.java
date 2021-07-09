package com.bhtcnpm.website.controller.Exercise;

import com.bhtcnpm.website.model.dto.ExerciseQuestion.ExerciseQuestionResultDTO;
import com.bhtcnpm.website.model.dto.ExerciseQuestion.ExerciseQuestionSubmitDTO;
import com.bhtcnpm.website.model.dto.ExerciseQuestion.ExerciseQuestionWithAnswersDTO;
import com.bhtcnpm.website.service.Exercise.ExerciseQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
public class ExerciseQuestionController {

    private final ExerciseQuestionService exerciseQuestionService;

    @GetMapping("/exercises/{exerciseID}/questionsAndAnswers")
    @ResponseBody
    public ResponseEntity<List<ExerciseQuestionWithAnswersDTO>> getExerciseQuestionsWithAnswers (@PathVariable("exerciseID") Long exerciseID) {
        List<ExerciseQuestionWithAnswersDTO> exerciseQuestionWithAnswersDTOs = exerciseQuestionService.getExerciseQuestionWithAnswers(exerciseID);

        return new ResponseEntity<>(exerciseQuestionWithAnswersDTOs, HttpStatus.OK);
    }

    @PostMapping("/exercises/{exerciseID}/attempt")
    @ResponseBody
    public ResponseEntity<List<ExerciseQuestionResultDTO>> submitAttempt (@PathVariable("exerciseID") Long exerciseID,
                                                                          @RequestBody List<ExerciseQuestionSubmitDTO> submitDTO,
                                                                          Authentication authentication) {
        List<ExerciseQuestionResultDTO> resultDTOs = exerciseQuestionService.submitAttemptAndGetResult(exerciseID, submitDTO, authentication);

        return new ResponseEntity<>(resultDTOs, HttpStatus.OK);
    }
}
