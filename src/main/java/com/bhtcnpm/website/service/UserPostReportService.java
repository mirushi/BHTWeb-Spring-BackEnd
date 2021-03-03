package com.bhtcnpm.website.service;

import com.bhtcnpm.website.model.dto.UserPostReport.UserPostReportDTO;
import com.bhtcnpm.website.model.dto.UserPostReport.UserPostReportListDTO;
import com.bhtcnpm.website.model.dto.UserPostReport.UserPostReportRequestDTO;
import com.bhtcnpm.website.model.dto.UserPostReport.UserPostReportResolveRequestDTO;
import com.bhtcnpm.website.model.exception.IDNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

@Validated
public interface UserPostReportService {
    Boolean createNewReport(Long userId, Long postId, @Valid UserPostReportRequestDTO dto) throws IDNotFoundException;
    Boolean resolveReport (Long userId, Long reportId, @Valid UserPostReportResolveRequestDTO dto) throws IDNotFoundException;
    UserPostReportListDTO getUserReports (Pageable pageable);
}
