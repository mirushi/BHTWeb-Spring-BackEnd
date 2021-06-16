package com.bhtcnpm.website.controller.Exercise;

import com.bhtcnpm.website.model.dto.Exercise.ExerciseSubjectSummaryDTO;
import com.bhtcnpm.website.model.dto.Exercise.ExerciseSubjectSummaryWithTopicAndExercisesDTO;
import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseSubject;
import com.bhtcnpm.website.service.Exercise.ExerciseSubjectService;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exercises/subjects")
@Validated
@RequiredArgsConstructor
public class ExerciseSubjectController {

    private final ExerciseSubjectService exerciseSubjectService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<ExerciseSubjectSummaryDTO>> getExerciseSubject (
            //This input is for doc displaying purpose only.
            //This is unknown why removing this cause the doc don't display QuerydslPredicate.
            //TODO: Find a way to refactor this.
            @RequestParam(required = false) Integer thisInputIsOptionalAndDontHaveEffect,
            @QuerydslPredicate(root = ExerciseSubject.class) Predicate predicate) {
        List<ExerciseSubjectSummaryDTO> exerciseSubjectSummaryDTOList = exerciseSubjectService.getExerciseSubject(predicate);

        return new ResponseEntity<>(exerciseSubjectSummaryDTOList, HttpStatus.OK);
    }

    //This is a cheat for speed-development of front-end.
    //This unusual API will increase coupling between backend and frontend.
    //Move the merging result operation to front-end.
    //This also makes the API became circular depending.
    //TODO: Please refactor this when you have time.
    @GetMapping("/fromExercise/{id}")
    @ResponseBody
    public ResponseEntity<ExerciseSubjectSummaryWithTopicAndExercisesDTO> getExerciseSubjectSummaryWithTopicAndExercise (@PathVariable("id") Long exerciseID) {
        ExerciseSubjectSummaryWithTopicAndExercisesDTO exerciseSubjectSummaryWithTopicAndExercisesDTO = exerciseSubjectService.getExerciseSubjectWithTopicAndExercises(exerciseID);

        return new ResponseEntity<>(exerciseSubjectSummaryWithTopicAndExercisesDTO, HttpStatus.OK);
    }
}
