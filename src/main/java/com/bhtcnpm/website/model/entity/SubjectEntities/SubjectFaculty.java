package com.bhtcnpm.website.model.entity.SubjectEntities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "subject_faculty")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubjectFaculty {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "subject_faculty_sequence"
    )
    @SequenceGenerator(
            name = "subject_faculty_sequence",
            sequenceName = "subject_faculty_sequence"
    )
    private Long id;

    @Column(name = "name")
    private String name;

    @Version
    private short version;

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof SubjectFaculty)) return false;
        SubjectFaculty other = (SubjectFaculty) o;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
