package com.bhtcnpm.website.model.entity.DocEntities;

import com.bhtcnpm.website.model.entity.enumeration.DocReaction.DocReactionType;
import com.bhtcnpm.website.model.entity.enumeration.DocReaction.DocReactionTypeConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "user_doc_reaction")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDocReaction {
    @EmbeddedId
    private UserDocReactionId userDocReactionId;

    @Column(columnDefinition = "smallint")
    @Enumerated
    private DocReactionType docReactionType;
}
