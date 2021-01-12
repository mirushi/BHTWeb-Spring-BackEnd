package com.bhtcnpm.website.model.entity;

import lombok.Data;

import javax.persistence.*;

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

    @Version
    private short version;
}
