package com.bhtcnpm.website.model.dto.Doc;

import com.bhtcnpm.website.model.entity.enumeration.DocReaction.DocReactionType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DocStatisticDTO {
    private Long docID;
    private Long likeCount;
    private Long dislikeCount;
    private DocReactionType docReactionType;
    private Long commentCount;
}
