package com.bhtcnpm.website.model.entity.ExerciseEntities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "exercise_report_reason")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExerciseReportReason {
    @EmbeddedId
    private ExerciseReportReasonId exerciseReportReasonId;
}
