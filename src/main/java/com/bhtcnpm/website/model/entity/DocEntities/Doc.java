package com.bhtcnpm.website.model.entity.DocEntities;

import com.bhtcnpm.website.constant.domain.Doc.DocApprovalState;
import com.bhtcnpm.website.constant.domain.Doc.DocBusinessState;
import com.bhtcnpm.website.model.entity.SubjectEntities.Subject;
import com.bhtcnpm.website.model.entity.Tag;
import com.bhtcnpm.website.model.entity.UserWebsite;
import com.bhtcnpm.website.model.entity.enumeration.DocState.DocStateType;
import com.bhtcnpm.website.repository.Doc.comparator.DocFileUploadComparatorRankBased;
import com.bhtcnpm.website.search.bridge.DocCategoryIDValueBridge;
import com.bhtcnpm.website.search.bridge.SubjectIDValueBridge;
import com.bhtcnpm.website.search.bridge.TagValueBridge;
import com.bhtcnpm.website.search.bridge.UserWebsiteIDValueBridge;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;
import org.hibernate.annotations.*;
import org.hibernate.search.engine.backend.types.*;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.ValueBridgeRef;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

@Entity(name = "Doc")
@Indexed
@Table(name = "doc")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE doc SET DELETED_DTM = CURRENT_TIMESTAMP() WHERE id = ? AND VERSION = ?")
@Loader(namedQuery = "findDocById")
@NamedQuery(name = "findDocById",
        query = "SELECT d FROM Doc d WHERE d.id = ?1 " +
                "AND d.deletedDtm IS NULL")
@Where(clause = "DELETED_DTM is NULL")
@NamedEntityGraph(
        name = "tagsAndDocFileUploads.all",
        attributeNodes = {
                @NamedAttributeNode(value = "tags"),
                @NamedAttributeNode(value = "docFileUploads")
        }
)
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
            valueBridge = @ValueBridgeRef(type = SubjectIDValueBridge.class),
            searchable = Searchable.YES,
            name = "subjectID"
    )
    private Subject subject;

    @Column(nullable = false)
    @FullTextField(analyzer = "default",
            norms = Norms.YES,
            termVector = TermVector.YES,
            projectable = Projectable.YES,
            searchable = Searchable.YES
    )
    private String description;

    @Lob
    @Column(columnDefinition = "text")
    private String adminFeedback;

    @OneToMany(
            mappedBy = "doc",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @SortComparator(value = DocFileUploadComparatorRankBased.class)
    private SortedSet<DocFileUpload> docFileUploads;

    @Column(nullable = false)
    private String imageURL;

    @Column(nullable = false)
    @GenericField(name = "publishDtm", sortable = Sortable.YES, projectable = Projectable.YES)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime publishDtm;

    @Column(nullable = false, updatable = false)
    @GenericField(sortable = Sortable.YES, projectable = Projectable.YES)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime submitDtm;

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
            name = "title_sort",
            norms = Norms.YES,
            sortable = Sortable.YES
    )
    private String title;

    @Enumerated
    @Column(name = "doc_state", columnDefinition = "smallint")
    @GenericField(projectable = Projectable.YES, searchable = Searchable.YES)
    private DocStateType docState;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @GenericField(name = "deletedDtm", searchable = Searchable.YES)
    @Column(name = "deleted_dtm")
    private LocalDateTime deletedDtm;

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

    @Column(name = "hotness", columnDefinition = "float(8)")
    @GenericField(sortable = Sortable.YES, projectable = Projectable.YES)
    private Double hotness = 0d;

    @Column(name = "wilson", columnDefinition = "float(8)")
    @GenericField(sortable = Sortable.YES, projectable = Projectable.YES)
    private Double wilson = 0d;

    @Column(name = "up_vote")
    @GenericField(name = "up_vote", sortable = Sortable.YES, projectable = Projectable.YES)
    private Long upVotes = 0L;

    @Column(name = "down_vote")
    @GenericField(name = "down_vote", sortable = Sortable.YES, projectable = Projectable.YES)
    private Long downVotes = 0L;

    @Column(name = "views")
    @GenericField(sortable = Sortable.YES, projectable = Projectable.YES)
    private Long views;

    @Column(name = "downloads")
    @GenericField(sortable = Sortable.YES, projectable = Projectable.YES)
    private Long downloads;

    @Version
    private short version;

    @Transient
    public DocBusinessState getDocBusinessState () {
        //Refer to BHTCNPM confluence. Entity state page.
        if (DocStateType.APPROVED.equals(this.getDocState())
                && deletedDtm == null
                && publishDtm.isBefore(LocalDateTime.now())) {
            return DocBusinessState.PUBLIC;
        }
        if (!DocStateType.APPROVED.equals(this.getDocState()) && deletedDtm == null
                || deletedDtm == null && publishDtm.isAfter(LocalDateTime.now())) {
            return DocBusinessState.UNLISTED;
        }
        if (deletedDtm != null) {
            return DocBusinessState.DELETED;
        }
        throw new UnsupportedOperationException("Cannot determine doc business state.");
    }

    @Transient
    public DocApprovalState getDocApprovalState() {
        if (DocStateType.APPROVED.equals(docState)) {
            return DocApprovalState.APPROVED;
        }
        if (DocStateType.REJECTED.equals(docState)) {
            return DocApprovalState.REJECTED;
        }
        return DocApprovalState.PENDING;
    }

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof Doc)) return false;
        Doc other = (Doc) o;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {return getClass().hashCode();}

    public void setDocFileUploads(List<DocFileUpload> docFileUploads) {
        if (this.docFileUploads != null) {
            this.docFileUploads.clear();
        } else {
            this.docFileUploads = new TreeSet<>(new DocFileUploadComparatorRankBased());
        }
        for (DocFileUpload file : docFileUploads) {
            file.setDoc(this);
        }
        this.docFileUploads.addAll(docFileUploads);
    }
}
