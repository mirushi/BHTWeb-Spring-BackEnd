package com.bhtcnpm.website.model.entity.DocEntities;

import com.bhtcnpm.website.model.entity.enumeration.DocReactionType.DocReactionType;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "user_doc_reaction")
@Data
public class UserDocReaction {
    @EmbeddedId
    private UserDocReactionId userDocReactionId;

    @Enumerated
    @Column(columnDefinition = "smallint")
    private DocReactionType docReactionType;
}
