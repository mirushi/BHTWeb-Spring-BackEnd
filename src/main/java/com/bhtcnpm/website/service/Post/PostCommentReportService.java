package com.bhtcnpm.website.service.Post;

import com.bhtcnpm.website.model.dto.PostCommentReport.PostCommentReportListDTO;
import com.bhtcnpm.website.model.dto.PostCommentReport.PostCommentReportRequestDTO;
import com.bhtcnpm.website.model.dto.PostCommentReport.PostCommentReportResolveRequestDTO;
import com.bhtcnpm.website.model.exception.IDNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import javax.validation.Valid;

public interface PostCommentReportService {
    @PreAuthorize(value = "hasPermission(#commentID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).POSTCOMMENT_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.PostCommentActionPermissionRequest).REPORT_PERMISSION)")
    boolean createNewReport (Long commentID, @Valid PostCommentReportRequestDTO dto, Authentication authentication);

    @PreAuthorize(value = "hasRole(T(com.bhtcnpm.website.constant.security.permission.PostCommentReportPermissionConstant).POSTCOMMENTREPORT_ALL_ALL_RESOLVE)")
    boolean resolveReport (Long reportID, @Valid PostCommentReportResolveRequestDTO dto, Authentication authentication) throws IDNotFoundException;

    @PreAuthorize(value = "hasRole(T(com.bhtcnpm.website.constant.security.permission.PostCommentReportPermissionConstant).POSTCOMMENTREPORT_ALL_ALL_RESOLVE)")
    PostCommentReportListDTO getUserReports(Pageable pageable, Boolean isResolved);
}
