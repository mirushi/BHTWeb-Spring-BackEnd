package com.bhtcnpm.website.model.entity;

import lombok.Data;
import org.apache.catalina.User;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
public class UserDocReactionId implements Serializable {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserWebsite user;

    @ManyToOne
    @JoinColumn(name = "doc_id")
    private Doc doc;

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDocReactionId)) return false;
        UserDocReactionId that = (UserDocReactionId) o;
        return Objects.equals(getUser(), that.getUser()) &&
                Objects.equals(getDoc(), that.getDoc());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUser(), getDoc());
    }
}
