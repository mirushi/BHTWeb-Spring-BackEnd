package com.bhtcnpm.website.model.dto.UserDocReaction;

import com.bhtcnpm.website.model.entity.enumeration.DocReactionType.DocReactionType;
import lombok.Data;

@Data
public class UserDocReactionDTO {
    private Long userId;

    private Long docId;

    private DocReactionType docReactionType;
}
