package com.bhtcnpm.website.service.Doc;

import com.bhtcnpm.website.model.dto.DocCommentReport.DocCommentReportListDTO;
import com.bhtcnpm.website.model.dto.DocCommentReport.DocCommentReportRequestDTO;
import com.bhtcnpm.website.model.dto.DocCommentReport.DocCommentReportResolveRequestDTO;
import com.bhtcnpm.website.model.exception.IDNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

public interface DocCommentReportService {
    @PreAuthorize(value = "hasPermission(#commentID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).DOCCOMMENT_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.DocCommentActionPermissionRequest).REPORT_PERMISSION)")
    boolean createNewReport (Long commentID, DocCommentReportRequestDTO dto, Authentication authentication);

    @PreAuthorize(value = "hasRole(T(com.bhtcnpm.website.constant.security.permission.DocCommentReportPermissionConstant).DOCCOMMENTREPORT_ALL_ALL_RESOLVE)")
    boolean resolveReport (Long reportID, DocCommentReportResolveRequestDTO dto, Authentication authentication) throws IDNotFoundException;

    @PreAuthorize(value = "hasRole(T(com.bhtcnpm.website.constant.security.permission.DocCommentReportPermissionConstant).DOCCOMMENTREPORT_ALL_ALL_RESOLVE)")
    DocCommentReportListDTO getUserReports (Pageable pageable, Boolean isResolved);
}
