package com.bhtcnpm.website.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
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
    private UserWebsite author;

    @OneToOne
    private DocCategory category;

    @ManyToOne
    private DocSubject subject;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Long downloadCount;

    @Column(nullable = false)
    private String imageURL;

    @Column(nullable = false)
    private LocalDateTime publishedDtm;

    @Column(nullable = false)
    private LocalDateTime createdDtm;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Long viewCount;

    @Column(nullable = false)
    private String docURL;

    @Column(nullable = false)
    private Boolean isApproved;

    @Column(nullable = false)
    private Boolean isSoftDeleted;

    @Column(nullable = false)
    private Boolean isPendingUserAction;

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

    @ManyToMany(cascade = {
            CascadeType.MERGE,
            CascadeType.PERSIST
    })
    @JoinTable(
            name = "doc_doc_subject",
            joinColumns = @JoinColumn(name = "doc_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    private Set<DocSubject> subjects;

    @OneToMany(
            mappedBy = "userDocReactionId.doc",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<UserDocReaction> userDocReactions;

    @Version
    private short version;

}
