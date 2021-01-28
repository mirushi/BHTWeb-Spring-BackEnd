package com.bhtcnpm.website.model.entity.PostEntities;

import com.bhtcnpm.website.model.entity.Tag;
import com.bhtcnpm.website.model.entity.UserWebsite;
import com.bhtcnpm.website.model.entity.enumeration.PostState.PostStateType;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "post")
@Data
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
    private String title;

    @Column(nullable = false)
    private String summary;

    @Column(nullable = false)
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

    @ManyToOne(fetch = FetchType.LAZY)
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
    private List<PostComment> comments;

    @OneToMany (
            mappedBy = "userPostLikeId.post",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<UserPostLike> userPostLikes;

    @OneToMany (
            mappedBy = "userPostSaveId.post",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
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
    private Set<Tag> tags;

    @Version
    private short version;
}
