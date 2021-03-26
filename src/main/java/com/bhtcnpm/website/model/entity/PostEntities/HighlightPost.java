package com.bhtcnpm.website.model.entity.PostEntities;

import com.bhtcnpm.website.model.entity.UserWebsite;
import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "highlight_post")
@Data
public class HighlightPost {
    @Id
    @OneToOne
    @JoinColumn(name = "post")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "highlighted_by_user")
    private UserWebsite highlightedBy;

    @Column(unique = true)
    private Integer rank;

    @NotNull
    private LocalDateTime highlightDtm;
}
