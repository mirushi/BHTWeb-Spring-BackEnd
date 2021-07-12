package com.bhtcnpm.website.model.entity.ExerciseEntities;

import com.bhtcnpm.website.constant.domain.ExerciseReport.ExerciseReportDomainConstant;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserExerciseReport {
    @EmbeddedId
    private UserExerciseReportId userExerciseReportId;

    @OneToMany(
            mappedBy = "exerciseReportReasonId.userExerciseReport",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Fetch(FetchMode.JOIN)
    private List<ExerciseReportReason> reasons;

    @Column(length = ExerciseReportDomainConstant.REPORT_FEEDBACK_LENGTH)
    private String feedback;
}
