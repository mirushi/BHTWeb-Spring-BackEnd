package com.bhtcnpm.website.model.entity.DocEntities;

import com.bhtcnpm.website.model.entity.UserWebsite;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Immutable
public class DocView {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "doc_view_sequence"
    )
    @SequenceGenerator(
            name = "doc_view_sequence",
            sequenceName = "doc_view_sequence"
    )
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false, updatable = false)
    private Doc doc;

    @ManyToOne
    @JoinColumn(updatable = false)
    private UserWebsite user;

    @Column(updatable = false)
    private String ipAddress;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime viewedAt;

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof DocView)) return false;
        DocView other = (DocView) o;

        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {return getClass().hashCode();}
}
