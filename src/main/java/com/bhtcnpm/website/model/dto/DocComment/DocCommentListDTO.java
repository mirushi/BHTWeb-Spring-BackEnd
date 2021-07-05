package com.bhtcnpm.website.model.dto.DocComment;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class DocCommentListDTO {
    List<DocCommentDTO> docCommentDTOs;
    Integer totalPages;
    Long totalElements;
}
