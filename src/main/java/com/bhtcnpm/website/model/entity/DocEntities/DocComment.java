package com.bhtcnpm.website.model.entity.DocEntities;

import com.bhtcnpm.website.model.entity.UserWebsite;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity(name = "DocComment")
@Table(name = "doc_comment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof DocComment)) return false;
        DocComment other = (DocComment) o;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
