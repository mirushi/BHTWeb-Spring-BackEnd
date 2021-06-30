package com.bhtcnpm.website.model.entity.DocCommentEntities;

import com.bhtcnpm.website.model.entity.DocEntities.Doc;
import com.bhtcnpm.website.model.entity.UserWebsite;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "doc_comment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE doc_comment SET DELETED_DATE = CURRENT_TIMESTAMP() WHERE id = ? AND VERSION = ?")
@Where(clause = "DELETED_DATE IS NULL")
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

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(nullable = false)
    private String content;

    @ManyToOne
    private UserWebsite author;

    @ManyToOne
    private Doc doc;

    @Column(name = "submit_dtm",
            nullable = false,
            updatable = false)
    @CreationTimestamp
    private LocalDateTime submitDtm = LocalDateTime.now();

    @Column(name = "last_edited_dtm",nullable = false)
    @UpdateTimestamp
    private LocalDateTime lastEditedDtm = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private DocComment parentComment;

    @OneToMany(
            mappedBy = "parentComment",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonManagedReference
    @EqualsAndHashCode.Exclude
    private List<DocComment> childComments;

    @OneToMany(
            mappedBy = "userDocCommentLikeId.docComment",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @EqualsAndHashCode.Exclude
    private Set<UserDocCommentLike> userDocCommentLikes;

    @Column(name = "deleted_date")
    private LocalDateTime deletedDate;

    @Version
    private short version;

    public DocCommentBusinessState getDocCommentBusinessState () {
        if (deletedDate == null) {
            return DocCommentBusinessState.PUBLIC;
        }
        if (deletedDate != null) {
            return DocCommentBusinessState.DELETE;
        }
        return null;
    }

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof DocComment)) return false;
        DocComment other = (DocComment) o;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {return getClass().hashCode();}
}
