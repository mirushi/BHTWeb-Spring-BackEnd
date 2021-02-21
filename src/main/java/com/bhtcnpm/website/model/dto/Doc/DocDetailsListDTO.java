package com.bhtcnpm.website.model.dto.Doc;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DocDetailsListDTO {
    private List<DocDetailsDTO> docDetails;
    private Integer totalPages;
    private Long totalElements;
}
