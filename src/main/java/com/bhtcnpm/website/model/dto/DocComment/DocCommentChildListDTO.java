package com.bhtcnpm.website.model.dto.DocComment;

import lombok.Value;

import java.util.List;

@Value
public class DocCommentChildListDTO {
    List<DocCommentChildDTO> docCommentChildDTOs;
    Integer totalPages;
    Long totalElements;
}
