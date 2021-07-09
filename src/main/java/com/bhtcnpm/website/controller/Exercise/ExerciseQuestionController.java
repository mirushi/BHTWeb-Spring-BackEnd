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
    public ResponseEntity<List<ExerciseQuestionPublicDTO>> createMultipleQuestions (@PathVariable("exerciseID") Long exerciseID,
                                                                                    @RequestBody List<ExerciseQuestionRequestDTO> requestDTOList,
                                                                                    Authentication authentication) {
        List<ExerciseQuestionPublicDTO> dtoList = exerciseQuestionService.createMultipleQuestions(exerciseID, requestDTOList, authentication);

        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @PostMapping("/exercises/{exerciseID}/questionsWithAnswers")
    @ResponseBody
    public ResponseEntity<List<ExerciseQuestionPublicWithAnswersDTO>> createMultipleQuestionsWithAnswers (@PathVariable("exerciseID") Long exerciseID,
                                                                                                          @RequestBody List<ExerciseQuestionRequestWithAnswersDTO> requestDTOList,
                                                                                                          Authentication authentication) {
        List<ExerciseQuestionPublicWithAnswersDTO> dtoList = exerciseQuestionService.createMultipleQuestionsWithAnswers(exerciseID, requestDTOList, authentication);

        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

}
