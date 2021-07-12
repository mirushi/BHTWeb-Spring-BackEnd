package com.bhtcnpm.website.model.entity.DocEntities;

import com.bhtcnpm.website.model.entity.enumeration.DocReaction.DocReactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDateTime lastReactionDtm;
}
