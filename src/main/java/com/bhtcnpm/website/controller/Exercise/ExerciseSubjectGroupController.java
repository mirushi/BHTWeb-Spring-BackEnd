package com.bhtcnpm.website.controller.Exercise;

import com.bhtcnpm.website.model.dto.Exercise.ExerciseSubjectGroupSummaryDTO;
import com.bhtcnpm.website.service.Exercise.ExerciseSubjectGroupService;
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
@RequestMapping("/exercises/subjectGroups")
@Validated
@RequiredArgsConstructor
public class ExerciseSubjectGroupController {

    private final ExerciseSubjectGroupService exerciseSubjectGroupService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<ExerciseSubjectGroupSummaryDTO>> getExerciseSubjectGroup () {
        List<ExerciseSubjectGroupSummaryDTO> exerciseSubjectGroupSummaryDTOs = exerciseSubjectGroupService.getExerciseSubjectGroup();

        return new ResponseEntity<>(exerciseSubjectGroupSummaryDTOs, HttpStatus.OK);
    }
}
