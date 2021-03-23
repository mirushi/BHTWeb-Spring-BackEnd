package com.bhtcnpm.website.model.entity;

import com.bhtcnpm.website.model.entity.DocEntities.Doc;
import com.bhtcnpm.website.model.entity.PostEntities.Post;
import com.bhtcnpm.website.model.entity.PostEntities.PostCategory;
import com.fasterxml.jackson.annotation.*;
import lombok.*;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;
import org.hibernate.search.engine.backend.types.Norms;
import org.hibernate.search.engine.backend.types.Searchable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;

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
@Indexed
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
    @KeywordField(norms = Norms.YES, searchable = Searchable.YES)
    private String content;

    @Version
    private short version;

    //For repository populator.
    @JsonCreator
    public static Tag getOne (@JacksonInject EntityManager entityManager, @JsonProperty("id") long id) {
        return entityManager.find(Tag.class, id);
    }
}
