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
public class HighlightPost {
    @EmbeddedId
    private HighlightPostId highlightPostId;

    @ManyToOne
    @JoinColumn(name = "highlighted_by_user")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private UserWebsite highlightedBy;

    private Integer rank;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime highlightDtm;
}
