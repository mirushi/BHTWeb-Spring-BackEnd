package com.bhtcnpm.website.service.Doc;

import com.bhtcnpm.website.model.dto.DocReport.DocReportListDTO;
import com.bhtcnpm.website.model.dto.DocReport.DocReportRequestDTO;
import com.bhtcnpm.website.model.dto.DocReport.DocReportResolveRequestDTO;
import com.bhtcnpm.website.model.exception.IDNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

public interface DocReportService {
    @PreAuthorize(value = "hasPermission(#docID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).DOC_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.DocActionPermissionRequest).REPORT_PERMISSION)")
    boolean createNewReport (Long docID, DocReportRequestDTO dto, Authentication authentication) throws IDNotFoundException;

    @PreAuthorize(value = "hasRole(T(com.bhtcnpm.website.constant.security.permission.DocReportPermissionConstant).DOCREPORT_ALL_ALL_RESOLVE)")
    boolean resolveReport (Long reportID, DocReportResolveRequestDTO dto, Authentication authentication) throws IDNotFoundException;

    @PreAuthorize(value = "hasRole(T(com.bhtcnpm.website.constant.security.permission.DocReportPermissionConstant).DOCREPORT_ALL_ALL_RESOLVE)")
    DocReportListDTO getUserReports (Pageable pageable, Boolean isResolved);
}
