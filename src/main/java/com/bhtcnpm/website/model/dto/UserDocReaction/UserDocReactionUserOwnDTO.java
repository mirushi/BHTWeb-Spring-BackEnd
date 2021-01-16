package com.bhtcnpm.website.model.dto.UserDocReaction;

import com.bhtcnpm.website.model.entity.enumeration.DocReactionType.DocReactionType;
import lombok.Data;

@Data
public class UserDocReactionUserOwnDTO {
    private Long docID;

    private DocReactionType docReactionType;
}
