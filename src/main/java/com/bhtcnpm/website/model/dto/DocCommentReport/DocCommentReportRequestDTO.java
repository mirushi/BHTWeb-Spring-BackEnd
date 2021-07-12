package com.bhtcnpm.website.model.dto.DocCommentReport;

import lombok.Value;

import java.util.List;

@Value
public class DocCommentReportRequestDTO {
    List<Long> reasonIds;
    String feedback;
}
