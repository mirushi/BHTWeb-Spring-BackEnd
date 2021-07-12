package com.bhtcnpm.website.controller.Exercise;

import com.bhtcnpm.website.model.dto.UserExerciseReport.UserExerciseReportListDTO;
import com.bhtcnpm.website.model.dto.UserExerciseReport.UserExerciseReportRequestDTO;
import com.bhtcnpm.website.model.dto.UserExerciseReport.UserExerciseReportResolveRequestDTO;
import com.bhtcnpm.website.model.exception.IDNotFoundException;
import com.bhtcnpm.website.service.Exercise.ExerciseReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;

@RestController
@Validated
@RequiredArgsConstructor
public class ExerciseReportController {

    private final ExerciseReportService exerciseReportService;

    @PostMapping("/exercises/{id}/report")
    @ResponseBody
    public ResponseEntity reportExercise (@PathVariable("id") Long exerciseID,
                                          @RequestBody UserExerciseReportRequestDTO dto,
                                          Authentication authentication) throws IDNotFoundException {
        exerciseReportService.createNewReport(exerciseID, dto, authentication);

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/exercises/resolveReport/{id}")
    @ResponseBody
    public ResponseEntity resolveReport (@PathVariable("id") Long reportID,
                                         @RequestBody UserExerciseReportResolveRequestDTO dto,
                                         Authentication authentication) throws IDNotFoundException{
        exerciseReportService.resolveReport(reportID, dto, authentication);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/exercises/report")
    @ResponseBody
    public ResponseEntity<UserExerciseReportListDTO> getUserReports (@PageableDefault @Nullable Pageable pageable,
                                                                     @Nullable Boolean isResolvedReport) {
        UserExerciseReportListDTO list = exerciseReportService.getUserReports(pageable, isResolvedReport);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
