package com.bhtcnpm.website.model.entity.ExerciseEntities;

import com.bhtcnpm.website.model.entity.UserWebsite;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "exercise_attempt")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseAttempt {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "exercise_attempt_sequence"
    )
    @SequenceGenerator(
            name = "exercise_attempt_sequence",
            sequenceName = "exercise_attempt_sequence"
    )
    private Long id;

    @Column(name = "attempt_dtm")
    private LocalDateTime attemptDtm;

    @Column(name = "correct_answered_questions")
    private Integer correctAnsweredQuestions;

    @Column(name = "total_score")
    private Integer totalScore;

    @ManyToOne
    private UserWebsite user;

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    @Version
    private short version;

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof ExerciseAttempt)) return false;
        ExerciseAttempt other = (ExerciseAttempt) o;

        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
