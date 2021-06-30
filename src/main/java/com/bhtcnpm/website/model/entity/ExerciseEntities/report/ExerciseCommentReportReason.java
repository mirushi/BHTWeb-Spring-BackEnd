package com.bhtcnpm.website.model.entity.ExerciseEntities.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "exercise_comment_report_reason")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseCommentReportReason {
    @EmbeddedId
    private ExerciseCommentReportReasonId exerciseCommentReportReasonId;
}
