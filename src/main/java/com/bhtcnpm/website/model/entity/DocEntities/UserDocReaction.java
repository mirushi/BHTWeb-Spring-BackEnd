package com.bhtcnpm.website.model.entity.DocEntities;

import com.bhtcnpm.website.model.entity.enumeration.DocReaction.DocReactionType;
import com.bhtcnpm.website.model.entity.enumeration.DocReaction.DocReactionTypeConverter;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "user_doc_reaction")
@Data
public class UserDocReaction {
    @EmbeddedId
    private UserDocReactionId userDocReactionId;

    @Column(columnDefinition = "smallint")
    @Enumerated
    private DocReactionType docReactionType;
}
