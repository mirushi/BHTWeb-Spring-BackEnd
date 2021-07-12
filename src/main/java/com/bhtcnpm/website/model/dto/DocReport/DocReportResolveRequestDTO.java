package com.bhtcnpm.website.model.dto.DocReport;

import com.bhtcnpm.website.model.entity.enumeration.DocReport.DocReportActionType;
import lombok.Value;

@Value
public class DocReportResolveRequestDTO {
    DocReportActionType docReportActionType;
    String resolvedNote;
}
