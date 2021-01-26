package com.bhtcnpm.website.model.dto.DocComment;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DocCommentListDTO {
    private List<DocCommentDTO> docCommentDTOs;

    private Integer totalPages;
}
