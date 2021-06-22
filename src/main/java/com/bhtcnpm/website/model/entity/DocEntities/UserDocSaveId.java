package com.bhtcnpm.website.model.entity.DocEntities;

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
public class UserDocSaveId implements Serializable {
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserWebsite user;

    @ManyToOne
    @JoinColumn(name = "doc_id")
    private Doc doc;

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDocSaveId)) return false;
        UserDocSaveId that = (UserDocSaveId) o;
        return Objects.equals(getUser(), that.getUser()) && Objects.equals(getDoc(), that.getDoc());
    }

    @Override
    public int hashCode() {return Objects.hash(getUser(), getDoc());}
}
