package com.bhtcnpm.website.model.dto.UserDocReaction;

import com.bhtcnpm.website.model.entity.enumeration.DocReaction.DocReactionType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDocReactionStatsDTO {
    private Long docID;
    private DocReactionType reactionType;
    private Long occurrences;
}
