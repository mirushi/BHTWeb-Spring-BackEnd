package com.bhtcnpm.website.repository.custom;

import com.bhtcnpm.website.model.dto.UserPostReport.UserPostReportListDTO;
import com.bhtcnpm.website.model.entity.PostEntities.UserPostReport;

import java.awt.print.Pageable;
import java.util.List;

public interface UserPostReportRepositoryCustom {
    List<UserPostReport> getUserReports (Pageable pageable, Boolean isResolved);
}
