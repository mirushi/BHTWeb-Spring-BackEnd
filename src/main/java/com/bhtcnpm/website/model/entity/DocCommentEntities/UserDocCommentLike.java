package com.bhtcnpm.website.model.entity.DocCommentEntities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "user_doc_comment_like")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDocCommentLike {
    @EmbeddedId
    private UserDocCommentLikeId userDocCommentLikeId;
}
