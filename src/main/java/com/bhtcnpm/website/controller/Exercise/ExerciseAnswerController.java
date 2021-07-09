package com.bhtcnpm.website.controller.Exercise;

import com.bhtcnpm.website.model.dto.ExerciseAnswer.ExerciseAnswerDTO;
import com.bhtcnpm.website.model.dto.ExerciseAnswer.ExerciseAnswerRequestContentOnlyDTO;
import com.bhtcnpm.website.model.dto.ExerciseAnswer.ExerciseAnswerRequestWithIDDTO;
import com.bhtcnpm.website.model.dto.ExerciseAnswer.ExerciseAnswerWithIsCorrectDTO;
import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseAnswer;
import com.bhtcnpm.website.service.Exercise.ExerciseAnswerService;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
public class ExerciseAnswerController {

    private final ExerciseAnswerService exerciseAnswerService;

    @GetMapping("/exercises/questions/{id}/answers")
    @ResponseBody
    public ResponseEntity<List<ExerciseAnswerDTO>> getExerciseAnswerByQuestionID (@PathVariable("id") Long questionID,
                                                                                  Authentication authentication) {
        List<ExerciseAnswerDTO> answers = exerciseAnswerService.getAnswerWithoutIsCorrect(questionID, authentication);

        return new ResponseEntity<>(answers, HttpStatus.OK);
    }

    @GetMapping("/exercises/questions/answers")
    @ResponseBody
    public ResponseEntity<List<ExerciseAnswerWithIsCorrectDTO>> getExerciseAnswerWithIsCorrect (@QuerydslPredicate(root = ExerciseAnswer.class)Predicate predicate,
                                                                                                @PageableDefault @Nullable Pageable pageable,
                                                                                                Authentication authentication) {
        List<ExerciseAnswerWithIsCorrectDTO> answerWithIsCorrectDTOs = exerciseAnswerService.getAnswerWithIsCorrect(predicate, pageable ,authentication);

        return new ResponseEntity<>(answerWithIsCorrectDTOs, HttpStatus.OK);
    }

    @PostMapping("/exercises/questions/{id}/answers")
    @ResponseBody
    public ResponseEntity<ExerciseAnswerWithIsCorrectDTO> createAnswer (@PathVariable("id") Long questionID,
                                                                        @RequestBody ExerciseAnswerRequestContentOnlyDTO exerciseAnswerRequestContentOnlyDTO,
                                                                        Authentication authentication) {
        ExerciseAnswerWithIsCorrectDTO dto = exerciseAnswerService.createAnswer(exerciseAnswerRequestContentOnlyDTO, questionID, authentication);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping("/exercises/questions/{id}/multipleAnswers")
    @ResponseBody
    public ResponseEntity<List<ExerciseAnswerWithIsCorrectDTO>> createMultipleAnswers (@PathVariable("id") Long questionID,
                                                                                 @RequestBody List<ExerciseAnswerRequestContentOnlyDTO> exerciseAnswerRequestContentOnlyDTOs,
                                                                                 Authentication authentication) {
        List<ExerciseAnswerWithIsCorrectDTO> dtoList = exerciseAnswerService.createMultipleAnswers(exerciseAnswerRequestContentOnlyDTOs, questionID, authentication);

        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @PutMapping("/exercises/questions/answers/{id}")
    @ResponseBody
    public ResponseEntity<ExerciseAnswerWithIsCorrectDTO> putExerciseAnswer (@PathVariable("id") Long exerciseAnswerID,
                                                                             @RequestBody ExerciseAnswerRequestContentOnlyDTO exerciseAnswerRequestContentOnlyDTO,
                                                                             Authentication authentication) {
        ExerciseAnswerWithIsCorrectDTO dto = exerciseAnswerService.updateAnswer(exerciseAnswerRequestContentOnlyDTO, exerciseAnswerID ,authentication);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping("/exercises/questions/answers")
    @ResponseBody
    public ResponseEntity<List<ExerciseAnswerWithIsCorrectDTO>> putMultipleAnswer (@RequestBody List<ExerciseAnswerRequestWithIDDTO> exerciseAnswerRequestWithIDDTOs,
                                                                                   Authentication authentication) {
        List<ExerciseAnswerWithIsCorrectDTO> dtoList = exerciseAnswerService.updateMultipleAnswers(exerciseAnswerRequestWithIDDTOs, authentication);

        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @DeleteMapping("/exercises/questions/answers/{id}")
    @ResponseBody
    public ResponseEntity deleteExerciseAnswer (@PathVariable("id") Long answerID,
                                                Authentication authentication) {
        exerciseAnswerService.deleteAnswer(answerID, authentication);

        return new ResponseEntity(HttpStatus.OK);
    }
}
