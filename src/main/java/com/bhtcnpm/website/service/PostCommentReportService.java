package com.bhtcnpm.website.service;

import com.bhtcnpm.website.model.dto.PostCommentReport.PostCommentReportListDTO;
import com.bhtcnpm.website.model.dto.PostCommentReport.PostCommentReportRequestDTO;
import com.bhtcnpm.website.model.dto.PostCommentReport.PostCommentReportResolveRequestDTO;
import com.bhtcnpm.website.model.exception.IDNotFoundException;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;
import java.util.UUID;

public interface PostCommentReportService {
    boolean createNewReport (UUID userID, Long commentID, @Valid PostCommentReportRequestDTO dto);
    boolean resolveReport (UUID userID, Long reportID, @Valid PostCommentReportResolveRequestDTO dto) throws IDNotFoundException;
    PostCommentReportListDTO getUserReports(Pageable pageable, Boolean isResolved);
}
