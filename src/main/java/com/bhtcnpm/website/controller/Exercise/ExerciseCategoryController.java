package com.bhtcnpm.website.controller.Exercise;

import com.bhtcnpm.website.model.dto.ExerciseCategory.ExerciseCategoryDTO;
import com.bhtcnpm.website.service.Exercise.ExerciseCategoryService;
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
@RequestMapping("/exercises/categories")
@Validated
@RequiredArgsConstructor
public class ExerciseCategoryController {

    private final ExerciseCategoryService exerciseCategoryService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<ExerciseCategoryDTO>> getExerciseCategories() {
        List<ExerciseCategoryDTO> exerciseCategoryDTOList = exerciseCategoryService.getExerciseCategories();

        return new ResponseEntity<>(exerciseCategoryDTOList, HttpStatus.OK);
    }
}
