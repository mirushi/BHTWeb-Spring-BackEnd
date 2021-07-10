package com.bhtcnpm.website.model.entity.ExerciseEntities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "exercise_question_difficulty_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseQuestionDifficulty {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "exercise_question_difficulty_type_sequence"
    )
    @SequenceGenerator(
            name = "exercise_question_difficulty_type_sequence",
            sequenceName = "exercise_question_difficulty_type_sequence"
    )
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "score")
    private Integer score;

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof ExerciseQuestionDifficulty)) return false;
        ExerciseQuestionDifficulty other = (ExerciseQuestionDifficulty) o;

        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() { return getClass().hashCode(); }
}
