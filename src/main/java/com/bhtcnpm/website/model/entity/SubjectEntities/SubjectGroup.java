package com.bhtcnpm.website.model.entity.SubjectEntities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "subject_group")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubjectGroup {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "subject_group_sequence"
    )
    @SequenceGenerator(
            name = "subject_group_sequence",
            sequenceName = "subject_group_sequence"
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
        if (!(o instanceof SubjectGroup)) return false;
        SubjectGroup other = (SubjectGroup) o;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() { return getClass().hashCode(); }
}
