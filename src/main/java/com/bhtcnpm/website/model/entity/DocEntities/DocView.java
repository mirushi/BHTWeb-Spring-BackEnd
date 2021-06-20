package com.bhtcnpm.website.model.entity.DocEntities;

import com.bhtcnpm.website.model.entity.UserWebsite;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    @JoinColumn(nullable = false, updatable = false)
    @ManyToOne
    private Doc doc;

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
        if (!(o instanceof DocView)) return false;
        DocView other = (DocView) o;

        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {return getClass().hashCode();}
}
