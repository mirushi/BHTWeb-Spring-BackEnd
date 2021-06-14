package com.bhtcnpm.website.model.entity.ExerciseEntities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "exercise_subject")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseSubject {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "exercise_subject_sequence"
    )
    @SequenceGenerator(
            name = "exercise_subject_sequence",
            sequenceName = "exercise_subject_sequence"
    )
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "imageURL")
    private String imageURL;

    @ManyToOne
    private ExerciseSubjectGroup subjectGroup;

    @ManyToOne
    private ExerciseSubjectFaculty subjectFaculty;

    @Version
    private short version;

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof ExerciseSubject)) return false;
        ExerciseSubject other = (ExerciseSubject) o;

        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
