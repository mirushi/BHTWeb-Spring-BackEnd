package com.bhtcnpm.website.model.entity;

import com.bhtcnpm.website.model.entity.DocEntities.Doc;
import com.bhtcnpm.website.model.entity.PostEntities.Post;
import com.bhtcnpm.website.model.entity.PostEntities.PostCategory;
import com.fasterxml.jackson.annotation.*;
import lombok.*;
import lombok.extern.jackson.Jacksonized;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;
import org.hibernate.search.engine.backend.types.Aggregable;
import org.hibernate.search.engine.backend.types.Norms;
import org.hibernate.search.engine.backend.types.Projectable;
import org.hibernate.search.engine.backend.types.Searchable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@NaturalIdCache
@Table(name = "tag")
//Making class becomes immutable for indexing inside other entities.
@RequiredArgsConstructor
@ToString
@AllArgsConstructor
@Builder
@Indexed
@Getter
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
    @GenericField(name = "id", searchable = Searchable.YES,
            aggregable = Aggregable.YES,
            projectable = Projectable.YES)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    @KeywordField(norms = Norms.YES,
            searchable = Searchable.YES,
            projectable = Projectable.YES)
    private String content;

    @ManyToMany(mappedBy = "tags")
    @EqualsAndHashCode.Exclude
    //We don't need this to be serialized.
    @ToString.Exclude
    @JsonIgnore
    private Set<Doc> docs;

    @ManyToMany (mappedBy = "tags")
    @EqualsAndHashCode.Exclude
    //We don't need this to be serialized.
    @ToString.Exclude
    @JsonIgnore
    @IndexedEmbedded(includeDepth = 2)
    private Set<Post> posts;

    @Version
    private short version;

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof Tag)) return false;
        Tag other = (Tag)o;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {return getClass().hashCode();}
}
