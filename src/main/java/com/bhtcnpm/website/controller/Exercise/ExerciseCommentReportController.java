package com.bhtcnpm.website.controller.Exercise;

import com.bhtcnpm.website.model.dto.ExerciseCommentReport.ExerciseCommentReportListDTO;
import com.bhtcnpm.website.model.dto.ExerciseCommentReport.ExerciseCommentReportRequestDTO;
import com.bhtcnpm.website.model.dto.ExerciseCommentReport.ExerciseCommentReportResolveRequestDTO;
import com.bhtcnpm.website.model.entity.ExerciseEntities.Exercise;
import com.bhtcnpm.website.service.ExerciseComment.ExerciseCommentReportService;
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
public class ExerciseCommentReportController {
    private final ExerciseCommentReportService exerciseCommentReportService;

    @PostMapping("/exercises/comments/{id}/report")
    @ResponseBody
    public ResponseEntity reportExerciseComment (@PathVariable("id") Long commentID,
                                                 @RequestBody ExerciseCommentReportRequestDTO dto,
                                                 Authentication authentication) {
        exerciseCommentReportService.createNewReport(commentID, dto, authentication);

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/exercises/comments/resolvedReport/{id}")
    @ResponseBody
    public ResponseEntity resolveReport (@PathVariable("id") Long reportID,
                                         @RequestBody ExerciseCommentReportResolveRequestDTO dto,
                                         Authentication authentication) {
        exerciseCommentReportService.resolveReport(reportID, dto, authentication);

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/exercises/comments/report")
    @ResponseBody
    public ResponseEntity<ExerciseCommentReportListDTO> getUserReports (@PageableDefault @Nullable Pageable pageable,
                                                                        @Nullable Boolean isResolvedReport) {
        ExerciseCommentReportListDTO list = exerciseCommentReportService.getUserReports(pageable, isResolvedReport);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
