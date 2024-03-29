package com.bhtcnpm.website.model.entity;

import com.bhtcnpm.website.constant.business.GenericBusinessConstant;
import com.bhtcnpm.website.constant.domain.UserWebsite.UWDomainConstant;
import com.bhtcnpm.website.model.entity.DocEntities.Doc;
import com.bhtcnpm.website.model.entity.DocEntities.UserDocReaction;
import com.bhtcnpm.website.model.entity.ExerciseEntities.Exercise;
import com.bhtcnpm.website.model.entity.PostEntities.Post;
import com.bhtcnpm.website.model.entity.PostEntities.UserPostLike;
import com.bhtcnpm.website.model.entity.PostEntities.UserPostSave;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Type;
import org.hibernate.search.engine.backend.types.Norms;
import org.hibernate.search.engine.backend.types.Projectable;
import org.hibernate.search.engine.backend.types.Searchable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "user_website")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserWebsite {
    //New account will automatically be created if not found in db.
    @Id
    @Column(columnDefinition = "uuid",
            nullable = false,
            unique = true)
    @GenericField(
            name = "id",
            searchable = Searchable.YES,
            projectable = Projectable.YES
    )
    private UUID id;

    @Column(nullable = false, length = UWDomainConstant.NAME_LENGTH)
    @Size(max = UWDomainConstant.NAME_LENGTH)
    @KeywordField(norms = Norms.YES, searchable = Searchable.YES)
    @NaturalId
    private String name;

    @Column(nullable = false, length = UWDomainConstant.EMAIL_LENGTH)
    @Size(max = UWDomainConstant.EMAIL_LENGTH)
    @Email
    private String email;

    @Column(nullable = false, length = UWDomainConstant.DISPLAY_NAME_LENGTH)
    @Size(max = UWDomainConstant.DISPLAY_NAME_LENGTH)
    @FullTextField(norms = Norms.YES, searchable = Searchable.YES)
    private String displayName;

    @Column(name = "reputation_score", nullable = false)
    private Long reputationScore;

    @Column(nullable = false, length = GenericBusinessConstant.URL_MAX_LENGTH)
    private String avatarURL;

    @Column(nullable = false)
    private String aboutMe;

    @OneToMany (
            mappedBy = "author",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private List<Post> postedPosts;

    @OneToMany (
            mappedBy = "author",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private List<Doc> postedDocs;

    @OneToMany(
            mappedBy = "author",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Exercise> postedExercises;

    @OneToMany(
            mappedBy = "userDocReactionId.user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private List<UserDocReaction> docReactions;

//    @OneToMany(
//            mappedBy = "actorPassive",
//            cascade = CascadeType.ALL,
//            orphanRemoval = true
//    )
//    @EqualsAndHashCode.Exclude
//    @ToString.Exclude
//    @JsonIgnore
//    private List<Notification> notificationsOfUserOwn;

    @OneToMany(
            mappedBy = "userPostLikeId.user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Set<UserPostLike> userPostLikes;

    @OneToMany (
            mappedBy = "userPostSaveId.user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Set<UserPostSave> userPostSaves;

//    @ManyToMany(mappedBy = "usersSaved")
//    @EqualsAndHashCode.Exclude
//    @ToString.Exclude
//    @JsonIgnore
//    private Set<Course> savedCourses;

    @Version
    private short version;

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof UserWebsite)) {
            return false;
        }
        UserWebsite other = (UserWebsite) o;

        return Objects.equals(getId(), other.getId());
    }

    @Override
    public int hashCode () {
        return Objects.hash(getId());
    }
}
