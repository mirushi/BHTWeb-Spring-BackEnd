package com.bhtcnpm.website.service.Exercise;

import com.bhtcnpm.website.model.dto.UserExerciseReport.UserExerciseReportListDTO;
import com.bhtcnpm.website.model.dto.UserExerciseReport.UserExerciseReportRequestDTO;
import com.bhtcnpm.website.model.dto.UserExerciseReport.UserExerciseReportResolveRequestDTO;
import com.bhtcnpm.website.model.exception.IDNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;

@Validated
public interface ExerciseReportService {
    Boolean createNewReport (Long exerciseID, UserExerciseReportRequestDTO dto, Authentication authentication) throws IDNotFoundException;
    Boolean resolveReport (Long reportID, UserExerciseReportResolveRequestDTO dto, Authentication authentication) throws IDNotFoundException;
    UserExerciseReportListDTO getUserReports (Pageable pageable, Boolean isResolved);
}
