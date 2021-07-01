package com.bhtcnpm.website.model.dto.UserPostReport;

import com.bhtcnpm.website.model.entity.enumeration.PostReportAction.PostReportActionType;
import com.bhtcnpm.website.model.validator.dto.PostReport.PostReportActionTypeEnum;
import com.bhtcnpm.website.model.validator.dto.PostReport.PostReportResolvedNote;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserPostReportResolveRequestDTO {
    @PostReportActionTypeEnum
    private PostReportActionType postReportActionType;
    @PostReportResolvedNote
    private String resolvedNote;
}
