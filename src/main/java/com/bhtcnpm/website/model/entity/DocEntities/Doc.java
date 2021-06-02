package com.bhtcnpm.website.model.entity.DocEntities;

import com.bhtcnpm.website.model.entity.*;
import com.bhtcnpm.website.model.entity.enumeration.DocState.DocStateType;
import com.bhtcnpm.website.search.bridge.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.*;
import org.hibernate.search.engine.backend.types.*;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.ValueBridgeRef;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Set;

@Entity(name = "Doc")
@Indexed
@Table(name = "doc")
@Data
@SQLDelete(sql = "UPDATE doc SET DELETED_DATE = "+ "20210302" +" WHERE id = ? AND VERSION = ?")
@Loader(namedQuery = "findDocById")
@NamedQuery(name = "findDocById",
        query = "SELECT d FROM Doc d WHERE d.id = ?1 " +
                "AND d.deletedDate IS NULL")
@Where(clause = "DELETED_DATE is NULL")
public class Doc {

    @Id
    @GeneratedValue (
            strategy = GenerationType.SEQUENCE,
            generator = "doc_sequence"
    )
    @SequenceGenerator(
            name = "doc_sequence",
            sequenceName = "doc_sequence"
    )
    @GenericField(name = "id", searchable = Searchable.YES, projectable = Projectable.YES)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    @IndexedEmbedded(name = "author")
    @GenericField(
            valueBridge = @ValueBridgeRef(type = UserWebsiteIDValueBridge.class),
            searchable = Searchable.YES,
            name = "authorID"
    )
    private UserWebsite author;

    @ManyToOne
    @JoinColumn
    private UserWebsite lastEditedUser;

    @ManyToOne
    @JoinColumn(nullable = false)
    @GenericField(
            valueBridge = @ValueBridgeRef(type = DocCategoryIDValueBridge.class),
            searchable = Searchable.YES,
            name = "categoryID"
    )
    private DocCategory category;

    @ManyToOne
    @JoinColumn(nullable = false)
    @GenericField(
            valueBridge = @ValueBridgeRef(type = DocSubjectIDValueBridge.class),
            searchable = Searchable.YES,
            name = "subjectID"
    )
    private DocSubject subject;

    @Column(nullable = false)
    @FullTextField(analyzer = "default",
            norms = Norms.YES,
            termVector = TermVector.YES,
            projectable = Projectable.YES,
            searchable = Searchable.YES
    )
    private String description;

    @OneToOne
    private DocFileUpload docFileUpload;

    @Column(nullable = false)
    private String imageURL;

    @Column(nullable = false)
    @GenericField(sortable = Sortable.YES, projectable = Projectable.YES)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime publishDtm;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    @GenericField(sortable = Sortable.YES, projectable = Projectable.YES)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdDtm;

    @Column(nullable = false)
    @UpdateTimestamp
    @GenericField(sortable = Sortable.YES, projectable = Projectable.YES)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime lastUpdatedDtm;

    @Column(nullable = false)
    @FullTextField(analyzer = "default",
            norms = Norms.YES,
            termVector = TermVector.YES,
            projectable = Projectable.YES,
            searchable = Searchable.YES
    )
    @KeywordField(
            name = "title_sort",norms = Norms.YES,
            sortable = Sortable.YES
    )
    private String title;

    @Column(nullable = false)
    private Long viewCount;

    @Enumerated
    @Column(columnDefinition = "smallint")
    @GenericField(projectable = Projectable.YES, searchable = Searchable.YES)
    private DocStateType docState;

    @ManyToMany
    @JoinTable(
            name = "doc_doc_tag",
            joinColumns = @JoinColumn(name = "doc_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @KeywordField(name = "tags_kw", searchable = Searchable.YES,
            valueBridge = @ValueBridgeRef(type = TagValueBridge.class))
    @IndexedEmbedded(name = "tags_eb")
    private Set<Tag> tags;

    @OneToMany(
            mappedBy = "userDocReactionId.doc",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<UserDocReaction> userDocReactions;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime deletedDate;

    @Version
    private short version;
}
