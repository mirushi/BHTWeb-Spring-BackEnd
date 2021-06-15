package com.bhtcnpm.website.controller.Exercise;

import com.bhtcnpm.website.model.dto.Exercise.ExerciseSubjectSummaryDTO;
import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseSubject;
import com.bhtcnpm.website.service.Exercise.ExerciseSubjectService;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
            @QuerydslPredicate(root = ExerciseSubject.class) Predicate predicate) {
        List<ExerciseSubjectSummaryDTO> exerciseSubjectSummaryDTOList = exerciseSubjectService.getExerciseSubject(predicate);

        return new ResponseEntity<>(exerciseSubjectSummaryDTOList, HttpStatus.OK);
    }
}
