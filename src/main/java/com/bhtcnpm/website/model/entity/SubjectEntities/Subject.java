package com.bhtcnpm.website.model.entity.SubjectEntities;

import com.bhtcnpm.website.model.entity.DocEntities.Doc;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "subject")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Subject {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "subject_sequence"
    )
    @SequenceGenerator(
            name = "subject_sequence",
            sequenceName = "subject_sequence",
            allocationSize = 3
    )
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false, length = 4096)
    private String description;

    @Column(name = "imageURL")
    private String imageURL;

    @ManyToOne
    @JoinColumn(name = "subject_group")
    private SubjectGroup subjectGroup;

    @ManyToOne
    @JoinColumn(name = "subject_faculty")
    private SubjectFaculty subjectFaculty;

    @Version
    private short version;

    @OneToMany(
            mappedBy = "subject",
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}
    )
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Set<Doc> docs;

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof Subject)) return false;
        Subject other = (Subject) o;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode () { return getClass().hashCode(); }
}
