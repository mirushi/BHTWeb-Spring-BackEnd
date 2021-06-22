package com.bhtcnpm.website.model.dto.UserDocReaction;

import com.bhtcnpm.website.model.entity.enumeration.DocReaction.DocReactionType;
import lombok.Data;

@Data
public class UserDocReactionUserOwnDTO {
    private DocReactionType docReactionType;
}
