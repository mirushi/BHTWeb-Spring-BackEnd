package com.bhtcnpm.website.model.entity.ExerciseEntities;

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
public class UserExerciseReportId implements Serializable {
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserWebsite user;

    @ManyToOne
    @JoinColumn(name = "exercise_report_id")
    private ExerciseReport exerciseReport;

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof UserExerciseReportId)) return false;
        UserExerciseReportId that = (UserExerciseReportId) o;
        return Objects.equals(getUser(), that.getUser())
                && Objects.equals(getExerciseReport(), that.getExerciseReport());
    }

    @Override
    public int hashCode() { return Objects.hash(getUser(), getExerciseReport()); }

}
