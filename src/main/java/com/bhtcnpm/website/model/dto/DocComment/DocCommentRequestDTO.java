package com.bhtcnpm.website.model.dto.DocComment;

import com.bhtcnpm.website.model.validator.dto.DocComment.DocCommentContent;
import lombok.Value;

@Value
public class DocCommentRequestDTO {
    @DocCommentContent
    String content;
}
