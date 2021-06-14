package com.bhtcnpm.website.model.entity.ExerciseEntities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "exercise_subject_faculty")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseSubjectFaculty {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "exercise_subject_faculty_sequence"
    )
    @SequenceGenerator(
            name = "exercise_subject_faculty_sequence",
            sequenceName = "exercise_subject_faculty_sequence"
    )
    private Long id;

    @Column(name = "name")
    private String name;

    @Version
    private short version;

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof ExerciseSubjectFaculty)) return false;
        ExerciseSubjectFaculty other = (ExerciseSubjectFaculty) o;

        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode () {
        return getClass().hashCode();
    }
}
