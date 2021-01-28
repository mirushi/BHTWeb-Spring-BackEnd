package com.bhtcnpm.website.model.entity.DocEntities;

import com.bhtcnpm.website.model.entity.*;
import com.bhtcnpm.website.model.entity.enumeration.DocState.DocStateType;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity(name = "Doc")
@Table(name = "doc")
@Data
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
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false, insertable = false, updatable = false)
    private UserWebsite author;

    @ManyToOne
    @JoinColumn(insertable = false, updatable = false)
    private UserWebsite lastEditedUser;

    @ManyToOne
    @JoinColumn(nullable = false, insertable = false, updatable = false)
    private DocCategory category;

    @ManyToOne
    @JoinColumn(nullable = false, insertable = false, updatable = false)
    private DocSubject subject;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Long downloadCount;

    @Column(nullable = false)
    private String imageURL;

    @Column(nullable = false)
    private LocalDateTime publishDtm;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDtm;

    @Column(nullable = false)
    private LocalDateTime lastEditDtm;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Long viewCount;

    @Column(nullable = false)
    private String docURL;

    @Enumerated
    @Column(columnDefinition = "smallint")
    private DocStateType docState;

    @ManyToMany(cascade = {
            CascadeType.MERGE,
            CascadeType.PERSIST
    })
    @JoinTable(
            name = "doc_doc_tag",
            joinColumns = @JoinColumn(name = "doc_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;

    @OneToMany(
            mappedBy = "userDocReactionId.doc",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<UserDocReaction> userDocReactions;

    @Version
    private short version;
}
