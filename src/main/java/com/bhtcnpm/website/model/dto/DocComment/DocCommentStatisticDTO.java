package com.bhtcnpm.website.model.dto.DocComment;

import lombok.Value;

@Value
public class DocCommentStatisticDTO {
    Long id;
    Long likeCount;
    Boolean likeStatus;
}
