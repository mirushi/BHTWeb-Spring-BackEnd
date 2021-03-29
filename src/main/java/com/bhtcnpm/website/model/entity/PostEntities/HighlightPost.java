package com.bhtcnpm.website.model.entity.PostEntities;

import com.bhtcnpm.website.model.entity.UserWebsite;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.sun.istack.NotNull;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "highlight_post")
@Getter
@Setter
@EqualsAndHashCode
@Builder
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class HighlightPost {
    @Id
    //Id cannot be set externally.
    @Setter(value = AccessLevel.NONE)
    private Long id;

    @OneToOne
    @JoinColumn(name = "post")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @PrimaryKeyJoinColumn
    private Post post;

    @ManyToOne
    @JoinColumn(name = "highlighted_by_user")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private UserWebsite highlightedBy;

    private Integer rank;

    @NotNull
    @CreationTimestamp
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime highlightDtm;

    //Workaround for Foreign Primary key problem when perform populating repository.
    //TODO: Figure out how this work.
    @JsonSetter("postID")
    public void setPost (Long id) { }

    public void setPost (Post post) {
        this.id = post.getId();
        this.post = post;
    }
}
