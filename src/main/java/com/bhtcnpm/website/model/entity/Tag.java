package com.bhtcnpm.website.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;
import org.springframework.data.domain.Persistable;

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

    @Column(nullable = false, unique = true, updatable = false)
    @NaturalId
    private String content;

    @ManyToMany(mappedBy = "tags")
    @EqualsAndHashCode.Exclude
    private Set<Doc> docs;

    @ManyToMany (mappedBy = "tags")
    @EqualsAndHashCode.Exclude
    private Set<Post> posts;

    @Version
    private short version;
}
