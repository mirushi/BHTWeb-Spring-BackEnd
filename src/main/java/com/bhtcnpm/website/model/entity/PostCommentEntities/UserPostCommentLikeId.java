package com.bhtcnpm.website.model.entity.PostCommentEntities;

import com.bhtcnpm.website.model.entity.UserWebsite;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPostCommentLikeId implements Serializable {
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserWebsite user;

    @ManyToOne
    @JoinColumn(name = "post_comment_id")
    private PostComment postComment;

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof UserPostCommentLikeId)) return false;
        UserPostCommentLikeId that = (UserPostCommentLikeId) o;
        return Objects.equals(getUser(), that.getUser())
                && Objects.equals(getPostComment(), that.getPostComment());
    }

    @Override
    public int hashCode () {
        return Objects.hash(getUser(), getPostComment());
    }
}
