package com.bhtcnpm.website.model.entity.PostEntities;

import com.bhtcnpm.website.model.entity.Tag;
import com.bhtcnpm.website.model.entity.UserWebsite;
import com.bhtcnpm.website.model.entity.enumeration.PostState.PostStateType;
import com.bhtcnpm.website.search.bridge.TagValueBridge;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Loader;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.search.engine.backend.types.*;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.ValueBridgeRef;
import org.hibernate.search.mapper.pojo.extractor.builtin.BuiltinContainerExtractors;
import org.hibernate.search.mapper.pojo.extractor.mapping.annotation.ContainerExtraction;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Indexed
@Table(name = "post")
@Data
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
    private LocalDateTime publishDtm;

    @ManyToOne
    private UserWebsite lastUpdatedBy;

    @Column
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

    @ManyToOne(fetch = FetchType.LAZY)
    @IndexedEmbedded
    private UserWebsite author;

    @ManyToOne(fetch = FetchType.LAZY)
    private PostCategory category;

    @Enumerated
    @Column(columnDefinition = "smallint")
    private PostStateType postState;

    @OneToMany(
            mappedBy = "post",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @EqualsAndHashCode.Exclude
    private List<PostComment> comments;

    @OneToMany (
            mappedBy = "userPostLikeId.post",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @EqualsAndHashCode.Exclude
    private Set<UserPostLike> userPostLikes;

    @OneToMany (
            mappedBy = "userPostSaveId.post",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @EqualsAndHashCode.Exclude
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
    @KeywordField(searchable = Searchable.YES,
            valueBridge = @ValueBridgeRef(type = TagValueBridge.class))
    private Set<Tag> tags;

    private LocalDateTime deletedDate;

    @Version
    private short version;
}
