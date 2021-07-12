package com.bhtcnpm.website.model.dto.DocCommentReport;

import com.bhtcnpm.website.model.entity.DocCommentEntities.report.DocCommentReport;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class DocCommentReportListDTO {
    List<DocCommentReportDTO> docCommentReportDTOs;
    Long totalElements;
    Integer totalPages;
}
