package com.bhtcnpm.website.controller.Exercise;

import com.bhtcnpm.website.model.dto.ExerciseQuestionDifficulty.ExerciseQuestionDifficultyDTO;
import com.bhtcnpm.website.service.Exercise.ExerciseQuestionDifficultyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/exercises/questions/difficulties")
@Validated
@RequiredArgsConstructor
public class ExerciseQuestionDifficultyController {

    private final ExerciseQuestionDifficultyService exerciseQuestionDifficultyService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<ExerciseQuestionDifficultyDTO>> getExerciseQuestionDifficulties () {
        List<ExerciseQuestionDifficultyDTO> dtoList = exerciseQuestionDifficultyService.getExerciseQuestionDifficultyList();

        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }
}
