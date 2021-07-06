package com.bhtcnpm.website.model.dto.DocReport;

import lombok.Value;

import java.util.List;

@Value
public class DocReportRequestDTO {
    List<Long> reasonIds;
    String feedback;
}
