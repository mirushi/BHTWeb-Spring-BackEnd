package com.bhtcnpm.website.model.entity.ExerciseEntities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "exercise_topic")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseTopic {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "exercise_topic_sequence"
    )
    @SequenceGenerator(
            name = "exercise_topic_sequence",
            sequenceName = "exercise_topic_sequence"
    )
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    private ExerciseSubject subject;

    @Version
    private short version;

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof ExerciseTopic)) return false;
        ExerciseTopic other = (ExerciseTopic) o;

        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
