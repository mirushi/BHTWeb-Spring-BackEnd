package com.bhtcnpm.website.model.dto.Doc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocDetailsWithStateListDTO {
    private List<DocDetailsWithStateDTO> docDetailsWithStateDTOs;
    private Integer totalPages;
    private Long totalElements;
}
