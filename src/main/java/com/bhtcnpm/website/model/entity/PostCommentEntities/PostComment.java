package com.bhtcnpm.website.model.entity.PostCommentEntities;

import com.bhtcnpm.website.constant.domain.PostComment.PostCommentBusinessState;
import com.bhtcnpm.website.model.entity.PostEntities.Post;
import com.bhtcnpm.website.model.entity.UserWebsite;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "post_comment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE post_comment SET DELETED_DTM = "+ "CURRENT_TIMESTAMP()" +" WHERE id = ? AND VERSION = ?")
@Where(clause = "DELETED_DTM is NULL")
public class PostComment {
    @Id
    @GeneratedValue (
            strategy = GenerationType.SEQUENCE,
            generator = "post_comment_sequence"
    )
    @SequenceGenerator(
            name = "post_comment_sequence",
            sequenceName = "post_comment_sequence"
    )
    private Long id;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "text", nullable = false)
    @Type(type = "org.hibernate.type.TextType")
    private String content;

    @ManyToOne
    private UserWebsite author;

    @ManyToOne
    private Post post;

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
    private PostComment parentComment;

    @OneToMany(
            mappedBy = "parentComment",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonManagedReference
    @EqualsAndHashCode.Exclude
    private List<PostComment> childComments;

    @OneToMany(
            mappedBy = "userPostCommentLikeId.postComment",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @EqualsAndHashCode.Exclude
    private Set<UserPostCommentLike> userPostCommentLikes;

    @Column(name = "deleted_dtm")
    private LocalDateTime deletedDtm;

    @Version
    private short version;

    @Transient
    public PostCommentBusinessState getPostCommentBusinessState() {
        if (deletedDtm == null) {
            return PostCommentBusinessState.PUBLIC;
        }
        if (deletedDtm != null) {
            return PostCommentBusinessState.DELETE;
        }
        return null;
    }

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof PostComment)) return false;
        PostComment other = (PostComment) o;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {return getClass().hashCode();}
}
