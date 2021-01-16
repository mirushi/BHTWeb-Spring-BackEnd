package com.bhtcnpm.website.model.entity;

import com.bhtcnpm.website.model.entity.enumeration.CourseContentType.CourseContentTypeConverter;
import com.bhtcnpm.website.model.entity.enumeration.DocReactionType.DocReactionType;
import com.bhtcnpm.website.model.entity.enumeration.DocReactionType.DocReactionTypeConverter;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class UserDocReaction {
    @EmbeddedId
    private UserDocReactionId userDocReactionId;

    @Enumerated
    @Column(columnDefinition = "smallint")
    private DocReactionType docReactionType;
}
