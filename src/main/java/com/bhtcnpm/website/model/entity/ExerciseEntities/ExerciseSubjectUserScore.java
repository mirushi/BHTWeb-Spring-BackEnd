package com.bhtcnpm.website.model.entity.ExerciseEntities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "exercise_subject_user_score")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseSubjectUserScore {
    @EmbeddedId
    private ExerciseSubjectUserScoreId exerciseSubjectUserScoreId;

    @Column(name = "total_score")
    private Long totalScore;
}
