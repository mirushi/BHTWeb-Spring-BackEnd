package com.bhtcnpm.website.model.entity.ExerciseEntities;

import com.bhtcnpm.website.model.entity.SubjectEntities.Subject;
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
public class ExerciseSubjectUserScoreId implements Serializable {
    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserWebsite user;

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof ExerciseSubjectUserScoreId)) return false;
        ExerciseSubjectUserScoreId that = (ExerciseSubjectUserScoreId) o;
        return Objects.equals(getSubject(), that.getSubject()) && Objects.equals(getUser(), that.getUser());
    }

    @Override
    public int hashCode() { return Objects.hash(getSubject(), getUser()); }
}
