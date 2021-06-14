package com.bhtcnpm.website.model.entity.ExerciseEntities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "exercise_subject_group")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseSubjectGroup {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "exercise_subject_group_sequence"
    )
    @SequenceGenerator(
            name = "exercise_subject_group_sequence",
            sequenceName = "exercise_subject_group_sequence"
    )
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Version
    private short version;

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof ExerciseSubjectGroup)) return false;
        ExerciseSubjectGroup other = (ExerciseSubjectGroup) o;

        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
