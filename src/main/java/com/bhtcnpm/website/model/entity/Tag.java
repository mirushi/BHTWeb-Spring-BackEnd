package com.bhtcnpm.website.model.entity;

import lombok.Data;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;

import javax.persistence.*;
import java.util.Set;

@Entity
@NaturalIdCache
@Table(name = "tag")
@Data
public class Tag {

    @Id
    @GeneratedValue (
            strategy = GenerationType.SEQUENCE,
            generator = "tag_sequence"
    )
    @SequenceGenerator(
            name = "tag_sequence",
            sequenceName = "tag_sequence"
    )
    private Long id;

    @Column(nullable = false)
    @NaturalId
    private String content;

    @ManyToMany(mappedBy = "tags")
    private Set<Doc> docs;

    @ManyToMany (mappedBy = "tags")
    private Set<Post> posts;

    @Version
    private short version;

}
