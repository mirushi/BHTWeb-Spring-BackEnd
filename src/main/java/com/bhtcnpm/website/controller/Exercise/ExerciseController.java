package com.bhtcnpm.website.controller.Exercise;

import com.bhtcnpm.website.model.dto.Exercise.*;
import com.bhtcnpm.website.model.entity.ExerciseEntities.Exercise;
import com.bhtcnpm.website.service.Exercise.ExerciseService;
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
@RequestMapping("/exercises")
@Validated
@RequiredArgsConstructor
public class ExerciseController {

    private final ExerciseService exerciseService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<ExerciseSummaryDTO>> getExerciseList (@QuerydslPredicate(root = Exercise.class) Predicate predicate,
                                                                     @PageableDefault @Nullable Pageable pageable,
                                                                     Authentication authentication) {
        List<ExerciseSummaryDTO> exerciseSummaryDTOs = exerciseService.getExerciseList(predicate, pageable, authentication);

        return new ResponseEntity<>(exerciseSummaryDTOs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<ExerciseDetailsDTO> getExerciseDetails (@PathVariable Long id) {
        ExerciseDetailsDTO exerciseDetailsDTO = exerciseService.getExerciseDetails(id);

        return new ResponseEntity<>(exerciseDetailsDTO, HttpStatus.OK);
    }

    @GetMapping("/statistics")
    @ResponseBody
    public ResponseEntity<List<ExerciseStatisticDTO>> getExercisesStatistics (@RequestParam List<Long> exerciseIDs) {
        List<ExerciseStatisticDTO> exerciseStatisticDTOList = exerciseService.getExerciseStatistics(exerciseIDs);

        return new ResponseEntity<>(exerciseStatisticDTOList, HttpStatus.OK);
    }

    @GetMapping("/statistics/user")
    @ResponseBody
    public ResponseEntity<List<ExerciseUserStatisticDTO>> getExerciseUserStatistics (@RequestParam List<Long> exerciseIDs,
                                                                               Authentication authentication) {
        List<ExerciseUserStatisticDTO> exerciseUserStatisticDTO = exerciseService.getExerciseUserStatistic(exerciseIDs, authentication);

        return new ResponseEntity<>(exerciseUserStatisticDTO, HttpStatus.OK);
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<ExerciseDetailsDTO> postExercise (@RequestBody ExerciseRequestDTO dto,
                                                            Authentication authentication) {
        ExerciseDetailsDTO exerciseDetails = exerciseService.createExercise(dto, authentication);

        return new ResponseEntity<>(exerciseDetails, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<ExerciseDetailsDTO> putExerciseDetails (@PathVariable Long id,
                                                                  @RequestBody ExerciseRequestDTO dto,
                                                                  Authentication authentication) {
        ExerciseDetailsDTO exerciseDetails = exerciseService.updateExercise(dto, id, authentication);

        return new ResponseEntity<>(exerciseDetails, HttpStatus.OK);
    }

    @DeleteMapping("/{exerciseID}")
    @ResponseBody
    public ResponseEntity deleteExercise (@PathVariable("exerciseID") Long exerciseID,
                                          Authentication authentication) {
        exerciseService.deleteExercise(exerciseID);

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/actionAvailable")
    @ResponseBody
    public ResponseEntity<List<ExerciseAvailableActionDTO>> getExerciseActionAvailable(@RequestParam List<Long> exerciseIDs,
                                                                                       Authentication authentication) {
        List<ExerciseAvailableActionDTO> availableActionDTOList = exerciseService.getAvailableExerciseAction(exerciseIDs, authentication);

        return new ResponseEntity<>(availableActionDTOList, HttpStatus.OK);
    }
}
