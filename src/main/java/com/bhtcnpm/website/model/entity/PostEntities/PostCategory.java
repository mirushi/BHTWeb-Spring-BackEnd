package com.bhtcnpm.website.model.entity.PostEntities;

import com.bhtcnpm.website.constant.domain.PostComment.PostCategoryDomainConstant;
import com.bhtcnpm.website.model.dto.Post.PostMapper;
import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;
import org.hibernate.search.engine.backend.types.Projectable;
import org.hibernate.search.engine.backend.types.Searchable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "post_category")
@Data
@NoArgsConstructor
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

    @NaturalId(mutable = true)
    @Column(nullable = false, length = PostCategoryDomainConstant.NAME_LENGTH)
    private String name;

    @Version
    private short version;
}
