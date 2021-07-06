package com.bhtcnpm.website.model.dto.DocReport;

import com.bhtcnpm.website.model.dto.DocCommentReport.DocCommentReportDTO;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class DocReportListDTO {
    List<DocReportDTO> docReportDTOs;
    Long totalElements;
    Integer totalPages;
}
