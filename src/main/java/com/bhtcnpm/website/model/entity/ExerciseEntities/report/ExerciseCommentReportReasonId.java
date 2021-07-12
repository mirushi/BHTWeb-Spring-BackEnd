package com.bhtcnpm.website.model.entity.ExerciseEntities.report;

import com.bhtcnpm.website.model.entity.ReportReason.ReportReason;
import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseCommentReportReasonId implements Serializable {
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
            @JoinColumn(name = "exercise_comment_report_id", referencedColumnName = "exercise_comment_report_id")
    })
    private UserExerciseCommentReport userExerciseCommentReport;

    @ManyToOne
    @JoinColumn(name = "report_reason_id")
    private ReportReason reportReason;

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof ExerciseCommentReportReasonId)) return false;
        ExerciseCommentReportReasonId that = (ExerciseCommentReportReasonId) o;
        return Objects.equals(getUserExerciseCommentReport(), that.getUserExerciseCommentReport())
                && Objects.equals(getReportReason(), that.getReportReason());
    }

    @Override
    public int hashCode () {return Objects.hash(getUserExerciseCommentReport(), getReportReason());}
}
