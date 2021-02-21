package com.bhtcnpm.website.model.dto.Doc;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DocSummaryListDTO {
    private List<DocSummaryDTO> docSummaries;
    private Integer totalPages;
    private Long totalElements;
}
