package com.bhtcnpm.website.model.entity.PostEntities;

import com.bhtcnpm.website.model.entity.DocEntities.Doc;
import com.bhtcnpm.website.model.entity.DocEntities.DocView;
import com.bhtcnpm.website.model.entity.UserWebsite;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostView {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "post_view_sequence"
    )
    @SequenceGenerator(
            name = "post_view_sequence",
            sequenceName = "post_view_sequence"
    )
    private Long id;

    @JoinColumn(nullable = false, updatable = false)
    @ManyToOne
    private Post post;

    @JoinColumn(updatable = false)
    @ManyToOne
    private UserWebsite user;

    @Column(updatable = false)
    private String ipAddress;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime viewedAt;

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof PostView)) return false;
        PostView other = (PostView) o;

        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {return getClass().hashCode();}
}
