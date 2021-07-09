package com.bhtcnpm.website.controller.Exercise;

import com.bhtcnpm.website.model.dto.ExerciseQuestion.*;
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

    @PostMapping("/exercises/{exerciseID}/questions")
    @ResponseBody
    public ResponseEntity<ExerciseQuestionPublicDTO> createQuestion (@PathVariable("exerciseID") Long exerciseID,
                                                                                @RequestBody ExerciseQuestionRequestDTO requestDTO,
                                                                                Authentication authentication) {
        ExerciseQuestionPublicDTO dto = exerciseQuestionService.createQuestion(exerciseID, requestDTO, authentication);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
