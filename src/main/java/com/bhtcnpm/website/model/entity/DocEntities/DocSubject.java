package com.bhtcnpm.website.model.entity.DocEntities;

import lombok.Data;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "doc_subject")
@Data
public class DocSubject {

    @Id
    @GeneratedValue (
            strategy = GenerationType.SEQUENCE,
            generator = "doc_subject_sequence"
    )
    @SequenceGenerator(
            name = "doc_subject_sequence",
            sequenceName = "doc_subject_sequence",
            allocationSize = 3
    )
    private Long id;

    @NaturalId(mutable = true)
    @Column(nullable = false)
    private String name;

    @OneToMany(
            mappedBy = "subject",
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}
    )
    private Set<Doc> docs;

    @Version
    private short version;
}
