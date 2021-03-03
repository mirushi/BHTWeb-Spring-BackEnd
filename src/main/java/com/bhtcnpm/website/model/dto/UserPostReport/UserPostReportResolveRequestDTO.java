package com.bhtcnpm.website.model.dto.UserPostReport;

import com.bhtcnpm.website.model.entity.enumeration.PostReportAction.PostReportActionType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserPostReportResolveRequestDTO {
    private PostReportActionType postReportActionType;
    private String resolvedNote;
}
