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
public class DocDownload {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "doc_download_sequence"
    )
    @SequenceGenerator(
            name = "doc_download_sequence",
            sequenceName = "doc_download_sequence"
    )
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false, updatable = false)
    private DocFileUpload docFileUpload;

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
        if (!(o instanceof DocDownload)) return false;
        DocDownload other = (DocDownload) o;

        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {return getClass().hashCode();}
}
