package com.bhtcnpm.website.model.dto.PostComment;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostCommentStatisticDTO {
    private Long id;
    private Long likeCount;
    private Boolean likeStatus;
}
