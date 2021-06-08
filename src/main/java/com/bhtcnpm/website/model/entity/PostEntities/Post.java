package com.bhtcnpm.website.model.entity.PostEntities;

import com.bhtcnpm.website.constant.domain.Post.PostApprovalState;
import com.bhtcnpm.website.constant.domain.Post.PostBusinessState;
import com.bhtcnpm.website.model.entity.Tag;
import com.bhtcnpm.website.model.entity.UserWebsite;
import com.bhtcnpm.website.model.entity.enumeration.PostState.PostStateType;
import com.bhtcnpm.website.search.bridge.UserWebsiteIDValueBridge;
import com.bhtcnpm.website.search.bridge.PostCategoryIDValueBridge;
import com.bhtcnpm.website.search.bridge.TagValueBridge;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;
import org.hibernate.annotations.Loader;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.search.engine.backend.types.*;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.ValueBridgeRef;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Indexed
@Table(name = "post")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE post SET DELETED_DATE = "+ "20210302" +" WHERE id = ? AND VERSION = ?")
@Loader(namedQuery = "findPostById")
@NamedQuery(name = "findPostById", query = "SELECT p FROM Post p WHERE p.id = ?1 AND p.deletedDate IS NULL")
@Where(clause = "DELETED_DATE is NULL")
public class Post {
    @Id
    @GeneratedValue (
            strategy = GenerationType.SEQUENCE,
            generator = "post_sequence"
    )
    @SequenceGenerator(
            name = "post_sequence",
            sequenceName = "post_sequence"
    )
    @GenericField(name = "id", searchable = Searchable.YES, projectable = Projectable.YES)
    private Long id;

    @Column(nullable = false)
    @FullTextField(analyzer = "default",
            norms = Norms.YES,
            termVector = TermVector.YES,
            projectable = Projectable.YES,
            searchable = Searchable.YES)
    @KeywordField(name = "title_sort",norms = Norms.YES,
            sortable = Sortable.YES)
    private String title;

    @Column(nullable = false)
    @FullTextField(analyzer = "default",
            norms = Norms.YES,
            termVector = TermVector.YES,
            projectable = Projectable.YES,
            searchable = Searchable.YES)
    private String summary;

    private String imageURL;

    @Column(nullable = false)
    @GenericField(sortable = Sortable.YES, projectable = Projectable.YES)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime publishDtm;

    @ManyToOne
    private UserWebsite lastUpdatedBy;

    @Column
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime lastUpdatedDtm;

    @Column(nullable = false)
    private Integer readingTime;

    @Lob
    @Column(nullable = false)
    private String content;

    @Lob
    @Column(nullable = false)
    @FullTextField(analyzer = "default",
            norms = Norms.YES,
            termVector = TermVector.YES,
            projectable = Projectable.YES,
            searchable = Searchable.YES)
    private String contentPlainText;

    @Lob
    @Column
    private String adminFeedback;

    @ManyToOne
    @IndexedEmbedded(name = "author")
    @GenericField(
            valueBridge = @ValueBridgeRef(type = UserWebsiteIDValueBridge.class),
            searchable = Searchable.YES,
            name = "authorID")
    private UserWebsite author;

    @ManyToOne
    @GenericField(
            valueBridge = @ValueBridgeRef(type = PostCategoryIDValueBridge.class),
            searchable = Searchable.YES,
            name = "categoryID"
    )
    private PostCategory category;

    @Enumerated
    @Column(columnDefinition = "smallint")
    @GenericField(projectable = Projectable.YES, searchable = Searchable.YES)
    private PostStateType postState;

    @OneToMany (
            mappedBy = "userPostLikeId.post",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Set<UserPostLike> userPostLikes;

    @OneToMany (
            mappedBy = "userPostSaveId.post",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Set<UserPostSave> userPostSaves;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(
            name = "post_post_tag",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @EqualsAndHashCode.Exclude
    @KeywordField(name = "tags_kw", searchable = Searchable.YES,
            valueBridge = @ValueBridgeRef(type = TagValueBridge.class))
    @IndexedEmbedded(name = "tags_eb")
    @ToString.Exclude
    @JsonIgnore
    private Set<Tag> tags;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @GenericField(searchable = Searchable.YES)
    private LocalDateTime deletedDate;

    @ManyToOne
    private UserWebsite deletedBy;

    @Version
    private short version;

    @Transient
    public boolean isDeleted () {
        //Post is deleted when deletedDate is not null.
        return this.getDeletedDate() != null;
    }

    @Transient
    public PostBusinessState getPostBusinessState() {
        //Refer to BHTCNPM confluence. Entity state page.
        if (PostStateType.APPROVED.equals(this.getPostState())
                && deletedDate == null
                && publishDtm.isBefore(LocalDateTime.now())) {
            return PostBusinessState.PUBLIC;
        }
        if (!PostStateType.APPROVED.equals(this.getPostState()) && deletedDate == null
                || this.deletedDate == null && publishDtm.isAfter(LocalDateTime.now())) {
            return PostBusinessState.UNLISTED;
        }
        if (deletedDate != null) {
            return PostBusinessState.DELETED;
        }
        return null;
    }

    @Transient
    public PostApprovalState getPostApprovalState() {
        if (PostStateType.APPROVED.equals(postState)) {
            return PostApprovalState.APPROVED;
        }
        if (PostStateType.REJECTED.equals(postState)) {
            return PostApprovalState.REJECTED;
        }
        return PostApprovalState.PENDING;
    }

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof Post)) return false;
        Post other = (Post)o;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode () {
        return getClass().hashCode();
    }
}
