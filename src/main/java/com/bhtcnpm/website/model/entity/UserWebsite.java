package com.bhtcnpm.website.model.entity;

import com.bhtcnpm.website.model.entity.DocEntities.Doc;
import com.bhtcnpm.website.model.entity.DocEntities.UserDocReaction;
import com.bhtcnpm.website.model.entity.PostEntities.Post;
import com.bhtcnpm.website.model.entity.PostEntities.UserPostLike;
import com.bhtcnpm.website.model.entity.PostEntities.UserPostSave;
import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "user_website")
@Data
public class UserWebsite {

    @Id
    @GeneratedValue (
            strategy = GenerationType.SEQUENCE,
            generator = "user_website_sequence"
    )
    @SequenceGenerator(
            name = "user_website_sequence",
            sequenceName = "user_website_sequence"
    )
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String displayName;

    @Column(nullable = false)
    private String hashedPassword;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private Long reputationScore;

    @ManyToOne
    @JoinColumn
    private UserWebsiteRole role;

    @Column(nullable = false)
    private String avatarURL;

    @Column(nullable = false)
    private Boolean banStatus;

    @OneToMany (
            mappedBy = "author",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Post> postedPosts;

    @OneToMany (
            mappedBy = "author",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Doc> postedDocs;

    @OneToMany(
            mappedBy = "userDocReactionId.user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<UserDocReaction> docReactions;

    @OneToMany(
            mappedBy = "actorPassive",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Notification> notificationsOfUserOwn;

    @OneToMany(
            mappedBy = "userPostLikeId.user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<UserPostLike> userPostLikes;

    @OneToMany (
            mappedBy = "userPostSaveId.user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<UserPostSave> userPostSaves;

    @ManyToMany(mappedBy = "usersSaved")
    private Set<Course> savedCourses;

    @Version
    private short version;
}
