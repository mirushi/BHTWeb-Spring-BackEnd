package com.bhtcnpm.website.model.entity.DocEntities;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "doc_category")
@Data
public class DocCategory {

    @Id
    @GeneratedValue (
            strategy = GenerationType.SEQUENCE,
            generator = "doc_category_sequence"
    )
    @SequenceGenerator(
                name = "doc_category_sequence",
            sequenceName = "doc_category_sequence",
            allocationSize = 3
    )
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany (
            mappedBy = "category",
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}
    )
    private Set<Doc> docs;

    @Version
    private short version;
}