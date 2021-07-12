package com.bhtcnpm.website.model.dto.UserPostReport;

import com.bhtcnpm.website.model.validator.dto.PostReport.PostReportFeedback;
import com.bhtcnpm.website.model.validator.dto.PostReport.PostReportReasonSize;
import com.bhtcnpm.website.model.validator.dto.ReportReason.ReportReasonID;
import lombok.Data;

import java.util.List;

@Data
public class UserPostReportRequestDTO {
    @PostReportReasonSize
    private List<@ReportReasonID Long> reasonIds;
    @PostReportFeedback
    private String feedback;
}
