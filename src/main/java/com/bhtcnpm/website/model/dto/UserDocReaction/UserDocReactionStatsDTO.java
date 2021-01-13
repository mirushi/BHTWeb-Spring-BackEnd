package com.bhtcnpm.website.model.dto.UserDocReaction;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDocReactionStatsDTO {
    private Long postID;
    private Long likeCount;
    private Long dislikeCount;
}
