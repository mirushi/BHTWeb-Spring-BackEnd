package com.bhtcnpm.website.model.entity.PostEntities;

import lombok.Data;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.List;

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
