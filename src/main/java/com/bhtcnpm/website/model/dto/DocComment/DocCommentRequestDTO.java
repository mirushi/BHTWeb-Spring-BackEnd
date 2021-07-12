package com.bhtcnpm.website.model.dto.DocComment;

import com.bhtcnpm.website.model.validator.dto.DocComment.DocCommentContent;
import lombok.*;

@Data
public class DocCommentRequestDTO {
    @DocCommentContent
    private String content;
}
