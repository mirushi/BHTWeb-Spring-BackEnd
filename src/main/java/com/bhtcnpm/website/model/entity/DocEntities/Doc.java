package com.bhtcnpm.website.model.entity.DocEntities;

import com.bhtcnpm.website.model.entity.*;
import com.bhtcnpm.website.model.entity.enumeration.DocState.DocStateType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import org.hibernate.annotations.Formula;

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
    @JoinColumn(nullable = false)
    private UserWebsite author;

    @ManyToOne
    @JoinColumn
    private UserWebsite lastEditedUser;

    @ManyToOne
    @JoinColumn(nullable = false)
    private DocCategory category;

    @ManyToOne
    @JoinColumn(nullable = false)
    private DocSubject subject;

    @Column(nullable = false)
    private String description;

    @OneToOne
    private DocFileUpload docFileUpload;

    @Column(nullable = false)
    private String imageURL;

    @Column(nullable = false)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime publishDtm;

    @Column(nullable = false, updatable = false)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdDtm;

    @Column(nullable = false)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime lastEditDtm;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Long viewCount;

    @Enumerated
    @Column(columnDefinition = "smallint")
    private DocStateType docState;

    @ManyToMany
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
