package com.bhtcnpm.website.model.entity.DocEntities;

import com.bhtcnpm.website.model.entity.UserWebsite;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity(name = "DocComment")
@Table(name = "doc_comment")
@Data
public class DocComment {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "doc_comment_sequence"
    )
    @SequenceGenerator(
            name = "doc_comment_sequence",
            sequenceName = "doc_comment_sequence"
    )
    private Long id;

    @ManyToOne
    private UserWebsite author;

    @Lob
    @Column(nullable = false)
    private String content;

    @ManyToOne
    private DocComment parentComment;

    @ManyToOne
    private Doc doc;

    @OneToMany(
            mappedBy = "parentComment",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<DocComment> childComments;
}
