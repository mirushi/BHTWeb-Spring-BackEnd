package com.bhtcnpm.website.model.entity.PostCommentEntities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "user_post_comment_like")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPostCommentLike {
    @EmbeddedId
    private UserPostCommentLikeId userPostCommentLikeId;
}
