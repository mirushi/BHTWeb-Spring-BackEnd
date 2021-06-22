package com.bhtcnpm.website.model.dto.Post;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostStatisticDTO {
    private Long id;
    private Long commentCount;
    private Long likeCount;
    private Long viewCount;
    private Boolean likeStatus;
    private Boolean savedStatus;
}
