package com.bhtcnpm.website.model.entity.ExerciseEntities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "exercise_question")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseQuestion {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "exercise_question_sequence"
    )
    @SequenceGenerator(
            name = "exercise_question_sequence",
            sequenceName = "exercise_question_sequence"
    )
    private Long id;

    @Column(name = "content")
    private String content;

    @Column(name = "rank")
    private Integer rank;

    @Column(name = "explaination")
    private String explaination;

    @ManyToOne
    private Exercise exercise;

    @Version
    private short version;

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof ExerciseQuestion)) return false;
        ExerciseQuestion other = (ExerciseQuestion) o;

        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
