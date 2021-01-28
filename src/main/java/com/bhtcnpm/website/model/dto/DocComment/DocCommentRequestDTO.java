package com.bhtcnpm.website.model.dto.DocComment;

import lombok.Data;

@Data
public class DocCommentRequestDTO {
    private String content;
    private Long parentCommentID;
}
