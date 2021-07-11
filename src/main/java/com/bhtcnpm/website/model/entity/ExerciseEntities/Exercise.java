package com.bhtcnpm.website.model.entity.ExerciseEntities;

import com.bhtcnpm.website.model.entity.Tag;
import com.bhtcnpm.website.model.entity.UserWebsite;
import com.bhtcnpm.website.model.entity.enumeration.ExerciseState.ExerciseStateType;
import com.bhtcnpm.website.search.bridge.ExerciseCategoryIDValueBridge;
import com.bhtcnpm.website.search.bridge.ExerciseTopicIDValueBridge;
import com.bhtcnpm.website.search.bridge.TagValueBridge;
import com.bhtcnpm.website.search.bridge.UserWebsiteIDValueBridge;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;
import org.hibernate.search.engine.backend.types.*;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.ValueBridgeRef;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Indexed
@Table(name = "exercise")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE exercise SET DELETED_DTM = CURRENT_TIMESTAMP() WHERE id = ? AND VERSION = ?")
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
    @GenericField(name = "id", searchable = Searchable.YES, projectable = Projectable.YES)
    private Long id;

    @Column(name = "title")
    @FullTextField(
            analyzer = "default",
            norms = Norms.YES,
            termVector = TermVector.YES,
            projectable = Projectable.YES,
            searchable = Searchable.YES
    )
    @KeywordField(
            name = "title_sort",
            norms = Norms.YES,
            sortable = Sortable.YES
    )
    private String title;

    @Column(name = "description")
    @FullTextField(
            analyzer = "default",
            norms = Norms.YES,
            termVector = TermVector.YES,
            projectable = Projectable.YES,
            searchable = Searchable.YES
    )
    private String description;

    @Column(name = "suggested_duration")
    @GenericField(sortable = Sortable.YES, projectable = Projectable.YES)
    private Integer suggestedDuration;

    @Column(name = "submit_dtm", nullable = false, updatable = false)
    @GenericField(sortable = Sortable.YES, projectable = Projectable.YES)
    private LocalDateTime submitDtm = LocalDateTime.now();

    @Column(name = "publish_dtm", nullable = false, updatable = false)
    @GenericField(sortable = Sortable.YES, projectable = Projectable.YES)
    private LocalDateTime publishDtm = LocalDateTime.now();

    @Column(name = "rank")
    @GenericField(sortable = Sortable.YES, projectable = Projectable.YES)
    private Integer rank;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    @IndexedEmbedded(name = "author")
    @GenericField(
            valueBridge = @ValueBridgeRef(type = UserWebsiteIDValueBridge.class),
            searchable = Searchable.YES,
            name = "authorID"
    )
    private UserWebsite author;

    @ManyToOne
    @JoinColumn(name = "last_updated_by")
    private UserWebsite lastUpdatedBy;

    @Column
    @GenericField(sortable = Sortable.YES, projectable = Projectable.YES)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @UpdateTimestamp
    private LocalDateTime lastUpdatedDtm = LocalDateTime.now();

    @ManyToOne
    @GenericField(
            valueBridge = @ValueBridgeRef(type = ExerciseTopicIDValueBridge.class),
            searchable = Searchable.YES,
            name = "topicID"
    )
    private ExerciseTopic topic;

    @ManyToOne
    @GenericField(
            valueBridge = @ValueBridgeRef(type = ExerciseCategoryIDValueBridge.class),
            searchable = Searchable.YES,
            name = "categoryID"
    )
    private ExerciseCategory category;

    @ManyToMany(cascade = {
                CascadeType.PERSIST,
                CascadeType.MERGE})
    @JoinTable(
            name = "exercise_exercise_tag",
            joinColumns = @JoinColumn(name = "exercise_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @KeywordField(name = "tags_kw", searchable = Searchable.YES,
            valueBridge = @ValueBridgeRef(type = TagValueBridge.class))
    @IndexedEmbedded(name = "tags_eb")
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
    @GenericField(projectable = Projectable.YES, searchable = Searchable.YES)
    private ExerciseStateType exerciseState;

    @Column(name = "deleted_dtm")
    @GenericField(name = "deletedDtm", searchable = Searchable.YES)
    private LocalDateTime deletedDtm;

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
