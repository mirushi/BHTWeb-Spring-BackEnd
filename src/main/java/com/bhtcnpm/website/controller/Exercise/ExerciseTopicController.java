package com.bhtcnpm.website.controller.Exercise;

import com.bhtcnpm.website.model.dto.Exercise.ExerciseTopicDTO;
import com.bhtcnpm.website.model.dto.Exercise.ExerciseTopicWithExerciseListDTO;
import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseTopic;
import com.bhtcnpm.website.service.Exercise.ExerciseTopicService;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
public class ExerciseTopicController {

    private final ExerciseTopicService exerciseTopicService;

    @GetMapping("/exercises/topics")
    @ResponseBody
    public ResponseEntity<List<ExerciseTopicDTO>> getExerciseTopics (
            //This input is for doc displaying purpose only.
            //It is unknown why removing this cause the doc don't display QuerydslPredicate.
            @RequestParam(required = false) Integer thisInputIsOptionalAndDontHaveEffect,
            @QuerydslPredicate(root = ExerciseTopic.class) Predicate predicate) {
        List<ExerciseTopicDTO> exerciseTopicDTOList = exerciseTopicService.getAllExerciseTopicsBySubject(predicate);

        return new ResponseEntity<>(exerciseTopicDTOList, HttpStatus.OK);
    }

    //This is a cheat for speed-development of front-end.
    //This unusual API will increase coupling between backend and frontend.
    //Move the merging result operation to front-end.
    //TODO: Please refactor this when you have time.
    @GetMapping("/exercises/topicsWithExercises")
    @ResponseBody
    public ResponseEntity<List<ExerciseTopicWithExerciseListDTO>> getExerciseTopicsWithExerciseList (
            @RequestParam Long subjectID, Authentication authentication
    ) {
        List<ExerciseTopicWithExerciseListDTO> exerciseTopicWithExerciseListDTOs = exerciseTopicService
                .getAllExerciseTopicsWithExerciseList(subjectID, authentication);

        return new ResponseEntity<>(exerciseTopicWithExerciseListDTOs, HttpStatus.OK);
    }
}
