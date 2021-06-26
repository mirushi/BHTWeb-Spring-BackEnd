package com.bhtcnpm.website.model.entity.ExerciseEntities;

import com.bhtcnpm.website.model.entity.ReportReason.ReportReason;
import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseReportReasonId implements Serializable {
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
            @JoinColumn(name = "exercise_report_id", referencedColumnName = "exercise_report_id")
    })
    private UserExerciseReport userExerciseReport;

    @ManyToOne
    @JoinColumn(name = "report_reason_id")
    private ReportReason reportReason;

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof ExerciseReportReasonId)) return false;
        ExerciseReportReasonId that = (ExerciseReportReasonId) o;
        return Objects.equals(getUserExerciseReport(), that.getUserExerciseReport())
                && Objects.equals(getReportReason(), that.getReportReason());
    }

    @Override
    public int hashCode() { return Objects.hash(getUserExerciseReport(), getReportReason()); }
}
