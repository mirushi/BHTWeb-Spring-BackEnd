package com.bhtcnpm.website.model.entity;

import com.bhtcnpm.website.constant.domain.UserWebsite.UWDomainConstant;
import com.bhtcnpm.website.model.entity.DocEntities.Doc;
import com.bhtcnpm.website.model.entity.DocEntities.UserDocReaction;
import com.bhtcnpm.website.model.entity.PostEntities.Post;
import com.bhtcnpm.website.model.entity.PostEntities.UserPostLike;
import com.bhtcnpm.website.model.entity.PostEntities.UserPostSave;
import lombok.*;
import org.hibernate.search.engine.backend.types.Norms;
import org.hibernate.search.engine.backend.types.Searchable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "user_website")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserWebsite implements UserDetails, CredentialsContainer {

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

    @Column(nullable = false, length = UWDomainConstant.NAME_LENGTH)
    @Size(max = UWDomainConstant.NAME_LENGTH)
    @KeywordField(norms = Norms.YES, searchable = Searchable.YES)
    private String name;

    @Column(nullable = false, length = UWDomainConstant.DISPLAY_NAME_LENGTH)
    @Size(max = UWDomainConstant.DISPLAY_NAME_LENGTH)
    @FullTextField(norms = Norms.YES, searchable = Searchable.YES)
    private String displayName;

    @Column(nullable = false, length = UWDomainConstant.PASSWORD_LENGTH)
    @Size(max = UWDomainConstant.PASSWORD_LENGTH)
    private String hashedPassword;

    @Column(nullable = false, length = UWDomainConstant.EMAIL_LENGTH)
    @Size(max = UWDomainConstant.EMAIL_LENGTH)
    @Email
    private String email;

    @Column(nullable = false)
    private Long reputationScore;

    @ManyToMany (
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
            fetch = FetchType.EAGER
    )
    @JoinTable (
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<UserWebsiteRole> roles;

    @Column(nullable = false)
    private String avatarURL;

    @Column(nullable = false)
    private Boolean banStatus;

    @OneToMany (
            mappedBy = "author",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @EqualsAndHashCode.Exclude
    private List<Post> postedPosts;

    @OneToMany (
            mappedBy = "author",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @EqualsAndHashCode.Exclude
    private List<Doc> postedDocs;

    @OneToMany(
            mappedBy = "userDocReactionId.user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @EqualsAndHashCode.Exclude
    private List<UserDocReaction> docReactions;

    @OneToMany(
            mappedBy = "actorPassive",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @EqualsAndHashCode.Exclude
    private List<Notification> notificationsOfUserOwn;

    @OneToMany(
            mappedBy = "userPostLikeId.user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @EqualsAndHashCode.Exclude
    private Set<UserPostLike> userPostLikes;

    @OneToMany (
            mappedBy = "userPostSaveId.user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @EqualsAndHashCode.Exclude
    private Set<UserPostSave> userPostSaves;

    @ManyToMany(mappedBy = "usersSaved")
    @EqualsAndHashCode.Exclude
    private Set<Course> savedCourses;

    @Transient
    @Builder.Default
    private Boolean accountNonExpired = true;

    @Transient
    @Builder.Default
    private Boolean credentialsNonExpired = true;

    @Transient
    @Builder.Default
    private Boolean enabled = true;

    @Version
    private short version;

    @Transient
    @Override
    public Set<GrantedAuthority> getAuthorities () {
        return this.roles.stream()
                .map(UserWebsiteRole::getAuthorities)
                .distinct()
                .flatMap(Set::stream)
                .map(authority -> {
                    return new SimpleGrantedAuthority(authority.getPermission());
                })
                .collect(Collectors.toSet());
    }

    @Override
    public void eraseCredentials() {
        this.hashedPassword = null;
    }

    @Override
    public String getPassword() {
        return this.hashedPassword;
    }

    @Override
    public String getUsername() {
        return this.name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.banStatus;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
