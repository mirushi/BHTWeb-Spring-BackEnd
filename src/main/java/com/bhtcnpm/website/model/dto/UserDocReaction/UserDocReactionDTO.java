package com.bhtcnpm.website.model.dto.UserDocReaction;

import com.bhtcnpm.website.model.entity.enumeration.DocReaction.DocReactionType;
import lombok.Data;

@Data
public class UserDocReactionDTO {
    private Long userID;

    private Long docID;

    private DocReactionType docReactionType;
}
