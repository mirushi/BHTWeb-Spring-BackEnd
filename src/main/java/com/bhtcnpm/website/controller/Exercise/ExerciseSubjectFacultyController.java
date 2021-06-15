package com.bhtcnpm.website.controller.Exercise;

import com.bhtcnpm.website.model.dto.Exercise.ExerciseSubjectFacultySummaryDTO;
import com.bhtcnpm.website.service.Exercise.ExerciseSubjectFacultyService;
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
@RequestMapping("/exercises/subjectFaculties")
@Validated
@RequiredArgsConstructor
public class ExerciseSubjectFacultyController {
    private final ExerciseSubjectFacultyService exerciseSubjectFacultyService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<ExerciseSubjectFacultySummaryDTO>> getAllExerciseSubjectFaculty () {
        List<ExerciseSubjectFacultySummaryDTO> exerciseSubjectFacultySummaryDTOs = exerciseSubjectFacultyService.getAllExerciseSubjectFaculty();

        return new ResponseEntity<>(exerciseSubjectFacultySummaryDTOs, HttpStatus.OK);
    }
}
