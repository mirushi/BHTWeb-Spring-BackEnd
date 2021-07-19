package com.bhtcnpm.website.model.dto.Doc;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
//TODO: Refactor this to be the same with DocSummaryWithStateAndFeedback.
public class DocSummaryWithStateListDTO {
    private List<DocSummaryWithStateDTO> docSummaryWithStateDTOs;
    private Integer totalPages;
    private Long totalElements;
}
