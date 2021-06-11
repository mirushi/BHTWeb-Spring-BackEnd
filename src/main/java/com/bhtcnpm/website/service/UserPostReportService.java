package com.bhtcnpm.website.service;

import com.bhtcnpm.website.model.dto.UserPostReport.UserPostReportListDTO;
import com.bhtcnpm.website.model.dto.UserPostReport.UserPostReportRequestDTO;
import com.bhtcnpm.website.model.dto.UserPostReport.UserPostReportResolveRequestDTO;
import com.bhtcnpm.website.model.exception.IDNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Validated
public interface UserPostReportService {
    @PreAuthorize(value = "hasPermission(#postID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).POST_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.PostActionPermissionRequest).REPORT_PERMISSION)")
    Boolean createNewReport(Long postID, @Valid UserPostReportRequestDTO dto, Authentication authentication) throws IDNotFoundException;

    @PreAuthorize(value = "hasRole(T(com.bhtcnpm.website.constant.security.permission.PostReportPermissionConstant).POSTREPORT_ALL_ALL_RESOLVE)")
    Boolean resolveReport (Long reportId, @Valid UserPostReportResolveRequestDTO dto, Authentication authentication) throws IDNotFoundException;

    @PreAuthorize(value = "hasRole(T(com.bhtcnpm.website.constant.security.permission.PostReportPermissionConstant).POSTREPORT_ALL_ALL_RESOLVE)")
    UserPostReportListDTO getUserReports (Pageable pageable, Boolean isResolved);
}
