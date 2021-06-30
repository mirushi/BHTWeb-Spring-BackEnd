package com.bhtcnpm.website.model.entity.DocCommentEntities;

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
public class UserDocCommentLikeId implements Serializable {
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserWebsite user;

    @ManyToOne
    @JoinColumn(name = "doc_comment_id")
    private DocComment docComment;

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDocCommentLikeId)) return false;
        UserDocCommentLikeId that = (UserDocCommentLikeId) o;
        return Objects.equals(getUser(), that.getUser())
                && Objects.equals(getDocComment(), that.getDocComment());
    }

    @Override
    public int hashCode() {return Objects.hash(getUser(), getDocComment());}
}
