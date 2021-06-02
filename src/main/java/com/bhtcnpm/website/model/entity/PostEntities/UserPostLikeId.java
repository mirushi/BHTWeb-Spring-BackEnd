package com.bhtcnpm.website.model.entity.PostEntities;

import com.bhtcnpm.website.model.entity.UserWebsite;
import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
public class UserPostLikeId implements Serializable {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserWebsite user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserPostLikeId)) return false;
        UserPostLikeId that = (UserPostLikeId) o;
        return Objects.equals(getUser(), that.getUser()) &&
                Objects.equals(getPost(), that.getPost());
    }

    @Override
    public int hashCode() {return Objects.hash(getUser(), getPost());}

}
