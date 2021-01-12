package com.bhtcnpm.website.model.entity;

import com.bhtcnpm.website.model.entity.enumeration.CourseContentType.CourseContentTypeConverter;
import com.bhtcnpm.website.model.entity.enumeration.DocReactionType.DocReactionType;
import com.bhtcnpm.website.model.entity.enumeration.DocReactionType.DocReactionTypeConverter;
import lombok.Data;

import javax.persistence.Convert;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@Data
public class UserDocReaction {
    @EmbeddedId
    private UserDocReactionId userDocReactionId;

    @Convert(converter = DocReactionTypeConverter.class)
    private DocReactionType docReactionType;
}
