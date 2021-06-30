package com.bhtcnpm.website.model.entity.ExerciseEntities.report;

import com.bhtcnpm.website.model.entity.DocCommentEntities.report.DocCommentReportReason;
import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseReportReason;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserExerciseCommentReport {
    @EmbeddedId
    private UserExerciseCommentReportId userExerciseCommentReportId;

    @OneToMany(
            mappedBy = "exerciseCommentReportReasonId.userExerciseCommentReport",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Fetch(FetchMode.JOIN)
    private List<ExerciseCommentReportReason> reasons;

    private String feedback;
}
