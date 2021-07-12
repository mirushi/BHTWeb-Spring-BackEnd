package com.bhtcnpm.website.service.ExerciseComment;

import com.bhtcnpm.website.model.dto.ExerciseCommentReport.ExerciseCommentReportListDTO;
import com.bhtcnpm.website.model.dto.ExerciseCommentReport.ExerciseCommentReportRequestDTO;
import com.bhtcnpm.website.model.dto.ExerciseCommentReport.ExerciseCommentReportResolveRequestDTO;
import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseComment;
import com.bhtcnpm.website.model.exception.IDNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

public interface ExerciseCommentReportService {
    @PreAuthorize(value = "hasPermission(#commentID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).EXERCISECOMMENT_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.ExerciseCommentActionPermissionRequest).REPORT_PERMISSION)")
    boolean createNewReport (Long commentID, ExerciseCommentReportRequestDTO dto, Authentication authentication);

    @PreAuthorize(value = "hasRole(T(com.bhtcnpm.website.constant.security.permission.ExerciseCommentReportPermissionConstant).EXERCISECOMMENTREPORT_ALL_ALL_RESOLVE)")
    boolean resolveReport (Long reportID, ExerciseCommentReportResolveRequestDTO dto, Authentication authentication) throws IDNotFoundException;

    @PreAuthorize(value = "hasRole(T(com.bhtcnpm.website.constant.security.permission.ExerciseCommentReportPermissionConstant).EXERCISECOMMENTREPORT_ALL_ALL_RESOLVE)")
    ExerciseCommentReportListDTO getUserReports (Pageable pageable, Boolean isResolved);
}
