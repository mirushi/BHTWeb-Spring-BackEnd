package com.bhtcnpm.website.model.dto.PostComment;

import com.bhtcnpm.website.model.validator.dto.PostComment.PostCommentContent;
import lombok.Data;

@Data
public class PostCommentRequestDTO {
    @PostCommentContent
    private String content;
}
