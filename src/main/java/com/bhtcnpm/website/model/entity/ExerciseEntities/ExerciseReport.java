package com.bhtcnpm.website.model.entity.ExerciseEntities;

import com.bhtcnpm.website.constant.domain.ExerciseReport.ExerciseReportDomainConstant;
import com.bhtcnpm.website.model.entity.UserWebsite;
import com.bhtcnpm.website.model.entity.enumeration.ExerciseReportAction.ExerciseReportActionType;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "exercise_report")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@NamedEntityGraph(
        name = "exerciseReport.all",
        attributeNodes = {
                @NamedAttributeNode(value = "userExerciseReports")
        }
)
public class ExerciseReport {
    @Id
    @GeneratedValue (
            strategy = GenerationType.SEQUENCE,
            generator = "exercise_report_sequence"
    )
    @SequenceGenerator(
            name = "exercise_report_sequence",
            sequenceName = "exercise_report_sequence"
    )
    private Long id;

    @ManyToOne
    @JoinColumn(name = "exercise_id", updatable = false)
    private Exercise exercise;

    @OneToMany(
            mappedBy = "userExerciseReportId.exerciseReport",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<UserExerciseReport> userExerciseReports;

    @Column(name = "report_time")
    @UpdateTimestamp
    private LocalDateTime reportTime;

    @Column(name = "resolved_time")
    private LocalDateTime resolvedTime;

    @JoinColumn(name = "resolved_by")
    @ManyToOne
    private UserWebsite resolvedBy;

    @Column(name = "resolved_note", length = ExerciseReportDomainConstant.RESOLVED_NOTE_LENGTH)
    private String resolvedNote;

    @Enumerated
    @Column(columnDefinition = "smallint")
    private ExerciseReportActionType actionTaken;

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof ExerciseReport)) return false;
        ExerciseReport that = (ExerciseReport) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() { return getClass().hashCode(); }
}
