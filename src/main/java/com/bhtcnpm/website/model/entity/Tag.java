package com.bhtcnpm.website.model.entity;

import com.bhtcnpm.website.model.entity.DocEntities.Doc;
import com.bhtcnpm.website.model.entity.PostEntities.Post;
import com.bhtcnpm.website.model.entity.PostEntities.PostCategory;
import com.fasterxml.jackson.annotation.*;
import lombok.*;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;

import javax.persistence.*;
import java.util.Set;

@Entity
@NaturalIdCache
@Table(name = "tag")
@Data
//Making class becomes immutable for indexing inside other entities.
@Setter(value = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(value = {"docs","posts"})
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

//    @ManyToMany(mappedBy = "tags")
//    @EqualsAndHashCode.Exclude
//    private Set<Doc> docs;
//
//    @ManyToMany (mappedBy = "tags")
//    @EqualsAndHashCode.Exclude
//    private Set<Post> posts;

    @Version
    private short version;

    //For repository populator.
    @JsonCreator
    public static Tag getOne (@JacksonInject EntityManager entityManager, @JsonProperty("id") long id) {
        return entityManager.find(Tag.class, id);
    }
}
