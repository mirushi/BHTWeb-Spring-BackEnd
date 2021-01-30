package com.bhtcnpm.website.model.dto.Doc;

import com.bhtcnpm.website.model.entity.enumeration.DocReaction.DocReactionType;
import com.bhtcnpm.website.model.entity.enumeration.DocReaction.DocReactionTypeConverter;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Convert;

@Data
@AllArgsConstructor
public class DocReactionStatisticDTO {
    private Long docID;
    private Long likeCount;
    private Long dislikeCount;
    private DocReactionType reactionType;

    public DocReactionStatisticDTO (Long docID, Long likeCount, Long dislikeCount, Integer reactionType) {
        this.docID = docID;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
        this.reactionType = null;
    }

}
