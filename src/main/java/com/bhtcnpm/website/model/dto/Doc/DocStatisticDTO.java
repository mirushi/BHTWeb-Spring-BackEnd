package com.bhtcnpm.website.model.dto.Doc;

import com.bhtcnpm.website.model.entity.enumeration.DocReactionType.DocReactionType;
import lombok.Data;

@Data
public class DocStatisticDTO {
    private Long docID;
    private Long likeCount;
    private Long dislikeCount;
    private DocReactionType reactionType;
    private Long commentCount;
}
