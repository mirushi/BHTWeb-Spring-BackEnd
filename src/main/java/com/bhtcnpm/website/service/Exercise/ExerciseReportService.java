package com.bhtcnpm.website.service.Exercise;

import com.bhtcnpm.website.model.dto.UserExerciseReport.UserExerciseReportListDTO;
import com.bhtcnpm.website.model.dto.UserExerciseReport.UserExerciseReportRequestDTO;
import com.bhtcnpm.website.model.dto.UserExerciseReport.UserExerciseReportResolveRequestDTO;
import com.bhtcnpm.website.model.exception.IDNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;

@Validated
public interface ExerciseReportService {
    @PreAuthorize(value = "hasPermission(#exerciseID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).EXERCISE_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.ExerciseActionPermissionRequest).REPORT_PERMISSION)")
    Boolean createNewReport (Long exerciseID, UserExerciseReportRequestDTO dto, Authentication authentication) throws IDNotFoundException;

    @PreAuthorize(value = "hasRole(T(com.bhtcnpm.website.constant.security.permission.ExerciseReportPermissionConstant).EXERCISEREPORT_ALL_ALL_RESOLVE)")
    Boolean resolveReport (Long reportID, UserExerciseReportResolveRequestDTO dto, Authentication authentication) throws IDNotFoundException;

    @PreAuthorize(value = "hasRole(T(com.bhtcnpm.website.constant.security.permission.ExerciseReportPermissionConstant).EXERCISEREPORT_ALL_ALL_RESOLVE)")
    UserExerciseReportListDTO getUserReports (Pageable pageable, Boolean isResolved);
}
