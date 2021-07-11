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
    public ResponseEntity<List<ExerciseQuestionWithAnswersPublicDTO>> getExerciseQuestionsWithAnswers (@PathVariable("exerciseID") Long exerciseID) {
        List<ExerciseQuestionWithAnswersPublicDTO> exerciseQuestionWithAnswersPublicDTOS = exerciseQuestionService.getPublicExerciseQuestionWithAnswers(exerciseID);

        return new ResponseEntity<>(exerciseQuestionWithAnswersPublicDTOS, HttpStatus.OK);
    }

    @GetMapping("/exercises/{exerciseID}/questions")
    @ResponseBody
    public ResponseEntity<List<ExerciseQuestionWithAnswersDTO>> getExerciseQuestionsWithAnswers (@PathVariable("exerciseID") Long exerciseID,
                                                                                                 Authentication authentication) {
        List<ExerciseQuestionWithAnswersDTO> dtoList = exerciseQuestionService.getExerciseQuestionsWithAnswers(exerciseID);

        return new ResponseEntity<>(dtoList, HttpStatus.OK);
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
    public ResponseEntity<List<ExerciseQuestionDTO>> createMultipleQuestions (@PathVariable("exerciseID") Long exerciseID,
                                                                              @RequestBody List<ExerciseQuestionRequestDTO> requestDTOList,
                                                                              Authentication authentication) {
        List<ExerciseQuestionDTO> dtoList = exerciseQuestionService.createMultipleQuestions(exerciseID, requestDTOList, authentication);

        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @PostMapping("/exercises/{exerciseID}/questionsWithAnswers")
    @ResponseBody
    public ResponseEntity<List<ExerciseQuestionWithAnswersDTO>> createMultipleQuestionsWithAnswers (@PathVariable("exerciseID") Long exerciseID,
                                                                                                    @RequestBody List<ExerciseQuestionRequestWithAnswersDTO> requestDTOList,
                                                                                                    Authentication authentication) {
        List<ExerciseQuestionWithAnswersDTO> dtoList = exerciseQuestionService.createMultipleQuestionsWithAnswers(exerciseID, requestDTOList, authentication);

        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @PutMapping("/exercises/{exerciseID}/questions")
    @ResponseBody
    public ResponseEntity<List<ExerciseQuestionDTO>> updateMultipleQuestions (@PathVariable("exerciseID") Long exerciseID,
                                                                              @RequestBody List<ExerciseQuestionRequestWithIDContentOnlyDTO> requestDTOList,
                                                                              Authentication authentication) {
        List<ExerciseQuestionDTO> dtoList = exerciseQuestionService.updateMultipleQuestions(requestDTOList, exerciseID, authentication);

        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @PutMapping("/exercises/{exerciseID}/questionsWithAnswers")
    @ResponseBody
    public ResponseEntity<List<ExerciseQuestionWithAnswersDTO>> updateMultipleQuestionsWithAnswers (@PathVariable("exerciseID") Long exerciseID,
                                                                                                    @RequestBody List<ExerciseQuestionRequestWithIDAndAnswersWithIDsDTO> requestDTOList,
                                                                                                    Authentication authentication) {
        List<ExerciseQuestionWithAnswersDTO> dtoList = exerciseQuestionService.updateMultipleQuestionsWithAnswers(requestDTOList, exerciseID ,authentication);

        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @DeleteMapping("/exercises/questions/{questionID}")
    @ResponseBody
    public ResponseEntity deleteQuestion (@PathVariable("questionID") Long questionID,
                                          Authentication authentication) {
        exerciseQuestionService.deleteQuestion(questionID, authentication);

        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/exercises/questions")
    @ResponseBody
    public ResponseEntity deleteMultipleQuestions (@RequestParam("ids") List<Long> questionIDs,
                                                   Authentication authentication) {
        exerciseQuestionService.deleteMultipleQuestions(questionIDs, authentication);

        return new ResponseEntity(HttpStatus.OK);
    }
}
