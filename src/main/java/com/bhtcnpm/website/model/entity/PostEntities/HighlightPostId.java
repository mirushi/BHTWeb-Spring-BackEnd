package com.bhtcnpm.website.model.entity.PostEntities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HighlightPostId implements Serializable {

    @OneToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof HighlightPostId)) return false;
        HighlightPostId that = (HighlightPostId) o;
        return Objects.equals(getPost(), that.getPost()) &&
                Objects.equals(getPost(), that.getPost());
    }

    @Override
    public int hashCode() {return Objects.hash(getPost());}

}
