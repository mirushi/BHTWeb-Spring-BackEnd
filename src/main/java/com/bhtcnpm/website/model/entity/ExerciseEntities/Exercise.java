package com.bhtcnpm.website.model.entity.ExerciseEntities;

import com.bhtcnpm.website.model.entity.Tag;
import com.bhtcnpm.website.model.entity.UserWebsite;
import com.bhtcnpm.website.model.entity.enumeration.ExerciseState.ExerciseStateType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Loader;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "exercise")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE exercise SET DELETED_DTM = CURRENT_TIMESTAMP() WHERE id = ? AND VERSION = ?")
//@Loader(namedQuery = "findExerciseById")
//@NamedQuery(
//        name = "findExerciseById",
//        query = "SELECT d FROM Doc d WHERE d.id = ?1 AND d.deletedDtm IS NULL "
//)
@Where(clause = "DELETED_DTM IS NULL")
public class Exercise {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "exercise_sequence"
    )
    @SequenceGenerator(
            name = "exercise_sequence",
            sequenceName = "exercise_sequence"
    )
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "suggested_duration")
    private Integer suggestedDuration;

    @Column(name = "submit_dtm", nullable = false, updatable = false)
    private LocalDateTime submitDtm = LocalDateTime.now();

    @Column(name = "publish_dtm", nullable = false, updatable = false)
    private LocalDateTime publishDtm = LocalDateTime.now();

    @Column(name = "rank")
    private Integer rank;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private UserWebsite author;

    @ManyToOne
    @JoinColumn(name = "last_updated_by")
    private UserWebsite lastUpdatedBy;

    @Column
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @UpdateTimestamp
    private LocalDateTime lastUpdatedDtm = LocalDateTime.now();

    @ManyToOne
    private ExerciseTopic topic;

    @ManyToOne
    private ExerciseCategory category;

    @ManyToMany(cascade = {
                CascadeType.PERSIST,
                CascadeType.MERGE})
    @JoinTable(
            name = "exercise_exercise_tag",
            joinColumns = @JoinColumn(name = "exercise_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;

    @OneToMany(
            mappedBy = "exercise",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<ExerciseAttempt> exerciseAttempts;

    @OneToMany(
            mappedBy = "exercise",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ExerciseQuestion> exerciseQuestions;

    @OneToMany(
            mappedBy = "exerciseNoteId.exercise",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ExerciseNote> exerciseNotes;

    @Enumerated
    @Column(name = "exercise_state", columnDefinition = "smallint", nullable = false)
    private ExerciseStateType exerciseState;

    @Column(name = "deleted_dtm")
    private LocalDateTime deletedDtm;

    @OneToMany(
            mappedBy = "exercise",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ExerciseComment> exerciseComments;

    @Version
    private short version;

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof Exercise)) return false;
        Exercise other = (Exercise) o;

        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
