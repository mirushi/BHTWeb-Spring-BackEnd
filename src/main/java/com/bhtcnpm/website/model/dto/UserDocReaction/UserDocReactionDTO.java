package com.bhtcnpm.website.model.dto.UserDocReaction;

import com.bhtcnpm.website.model.entity.enumeration.DocReaction.DocReactionType;
import lombok.Data;

import java.util.UUID;

@Data
public class UserDocReactionDTO {
    private UUID userID;

    private Long docID;

    private DocReactionType docReactionType;
}
