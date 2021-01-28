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
public class UserPostSaveId implements Serializable {
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserWebsite user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof UserPostSaveId)) return false;
        UserPostSaveId that = (UserPostSaveId) o;
        return Objects.equals(getUser(), that.getUser()) &&
                Objects.equals(getPost(), that.getPost());
    }

    @Override
    public int hashCode() {return Objects.hash(getUser(), getPost());}
}
