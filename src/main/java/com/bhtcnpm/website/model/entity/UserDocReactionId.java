package com.bhtcnpm.website.model.entity;

import lombok.Data;
import org.apache.catalina.User;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
public class UserDocReactionId implements Serializable {
    @ManyToOne
    private UserWebsite user;

    @ManyToOne
    private Doc doc;

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDocReactionId)) return false;
        UserDocReactionId that = (UserDocReactionId) o;
        return Objects.equals(user.getId(), that.user.getId()) &&
                Objects.equals(doc.getId(), that.doc.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(user.getId(), doc.getId());
    }
}
