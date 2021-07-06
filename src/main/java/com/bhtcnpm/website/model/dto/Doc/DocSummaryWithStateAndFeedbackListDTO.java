package com.bhtcnpm.website.model.dto.Doc;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class DocSummaryWithStateAndFeedbackListDTO {
    List<DocSummaryWithStateAndFeedbackDTO> docSummaryWithStateAndFeedbackDTOs;
    Integer totalPages;
    Long totalElements;
}
