package com.bhtcnpm.website.model.entity;

import lombok.Data;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "post_category")
@Data
public class PostCategory {

    @Id
    @GeneratedValue (
            strategy = GenerationType.SEQUENCE,
            generator = "post_category_sequence"
    )
    @SequenceGenerator(
            name = "post_category_sequence",
            sequenceName = "post_category_sequence",
            allocationSize = 3
    )
    private Long id;

    @NaturalId
    @Column(nullable = false)
    private String name;

    @OneToMany (
        mappedBy = "category",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<Post> posts;

    @Version
    private short version;
}
