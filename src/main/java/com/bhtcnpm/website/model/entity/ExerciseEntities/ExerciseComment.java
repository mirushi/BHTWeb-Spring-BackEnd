package com.bhtcnpm.website.model.entity.ExerciseEntities;

import com.bhtcnpm.website.constant.domain.ExerciseComment.ExerciseCommentBusinessState;
import com.bhtcnpm.website.model.entity.UserWebsite;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "exercise_comment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE exercise_comment SET DELETED_DATE = CURRENT_TIMESTAMP() WHERE id = ? AND VERSION = ?")
@Where(clause = "DELETED_DATE is NULL")
public class ExerciseComment {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "exercise_comment_sequence"
    )
    @SequenceGenerator(
            name = "exercise_comment_sequence",
            sequenceName = "exercise_comment_sequence"
    )
    private Long id;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "text", nullable = false)
    private String content;

    @ManyToOne
    private UserWebsite author;

    @ManyToOne
    private Exercise exercise;

    @Column(name = "submit_dtm",nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime submitDtm = LocalDateTime.now();

    @Column(name = "last_edited_dtm", nullable = false)
    @UpdateTimestamp
    private LocalDateTime lastEditedDtm = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private ExerciseComment parentComment;

    @OneToMany(
            mappedBy = "parentComment",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonManagedReference
    private List<ExerciseComment> childComments;

    @OneToMany(
            mappedBy = "userExerciseCommentLikeId.exerciseComment",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<UserExerciseCommentLike> userExerciseCommentLikes;

    @Column(name = "deleted_date")
    private LocalDateTime deletedDate;

    @Version
    private short version;

    @Transient
    public ExerciseCommentBusinessState getExerciseCommentBusinessState () {
        if (deletedDate == null) {
            return ExerciseCommentBusinessState.PUBLIC;
        }
        if (deletedDate != null) {
            return ExerciseCommentBusinessState.DELETE;
        }
        return null;
    }

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof ExerciseComment)) return false;
        ExerciseComment other = (ExerciseComment) o;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {return getClass().hashCode();}
}
