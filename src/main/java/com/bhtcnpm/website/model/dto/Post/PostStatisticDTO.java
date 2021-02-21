package com.bhtcnpm.website.model.dto.Post;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostStatisticDTO {
    private Long id;
    private Long commentCount;
    private Long likeCount;
    private Boolean likeStatus;
    private Boolean savedStatus;
}
