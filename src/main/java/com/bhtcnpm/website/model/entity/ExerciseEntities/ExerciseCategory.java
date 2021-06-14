package com.bhtcnpm.website.model.entity.ExerciseEntities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "exercise_category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseCategory {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "exercise_category_sequence"
    )
    @SequenceGenerator(
            name = "exercise_category_sequence",
            sequenceName = "exercise_category_sequence"
    )
    private Long id;

    @Column(name = "name")
    private String name;

    @Version
    private short version;

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof ExerciseCategory)) return false;
        ExerciseCategory other = (ExerciseCategory) o;

        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
