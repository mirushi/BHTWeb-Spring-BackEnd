package com.bhtcnpm.website.model.entity.ExerciseEntities.report;

import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseComment;
import com.bhtcnpm.website.model.entity.UserWebsite;
import com.bhtcnpm.website.model.entity.enumeration.ExerciseCommentReport.ExerciseCommentReportActionType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@NamedEntityGraph(
        name = "exerciseCommentReport.all",
        attributeNodes = {
                @NamedAttributeNode(value = "userExerciseCommentReports")
        }
)
public class ExerciseCommentReport {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "exercise_comment_report_sequence"
    )
    @SequenceGenerator(
            name = "exercise_comment_report_sequence",
            sequenceName = "exercise_comment_report_sequence"
    )
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "exercise_comment_id",
            updatable = false
    )
    private ExerciseComment exerciseComment;

    @OneToMany(
            mappedBy = "userExerciseCommentReportId.exerciseCommentReport",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<UserExerciseCommentReport> userExerciseCommentReports;

    @Column(name = "report_time")
    private LocalDateTime reportTime;

    @Column(name = "resolved_time")
    private LocalDateTime resolvedTime;

    @JoinColumn(name = "resolved_by")
    @ManyToOne
    private UserWebsite resolvedBy;

    @Column(name = "resolved_note")
    private String resolvedNote;

    @Enumerated
    @Column(columnDefinition = "smallint")
    private ExerciseCommentReportActionType actionTaken;

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof ExerciseCommentReport)) return false;
        ExerciseCommentReport that = (ExerciseCommentReport) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {return getClass().hashCode();}
}
