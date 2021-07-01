package com.bhtcnpm.website.model.entity.PostEntities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.search.engine.backend.types.Projectable;
import org.hibernate.search.engine.backend.types.Searchable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostViewAvg {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "post_view_avg_sequence"
    )
    @SequenceGenerator(
            name = "post_view_avg_sequence",
            sequenceName = "post_view_avg_sequence"
    )
    @GenericField(name = "id", searchable = Searchable.YES, projectable = Projectable.YES)
    private Long id;

    @OneToOne
    @JoinColumn(nullable = false)
    private Post post;

    @Column(nullable = false)
    private Long pastView;

    @Column(nullable = false)
    //TODO: This will handle for about 1 billion view max.
    private Long sqrPastView;

    @Column(nullable = false)
    private Double recentAvg;
}
