package com.bhtcnpm.website.model.dto.DocCommentReport;

import com.bhtcnpm.website.model.entity.enumeration.DocCommentReport.DocCommentReportActionType;
import lombok.Value;

@Value
public class DocCommentReportResolveRequestDTO {
    DocCommentReportActionType docCommentReportActionType;
    String resolvedNote;
}
