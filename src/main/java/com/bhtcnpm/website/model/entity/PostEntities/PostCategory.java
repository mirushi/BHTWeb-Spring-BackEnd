package com.bhtcnpm.website.model.entity.PostEntities;

import com.bhtcnpm.website.constant.domain.PostComment.PostCategoryDomainConstant;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

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
