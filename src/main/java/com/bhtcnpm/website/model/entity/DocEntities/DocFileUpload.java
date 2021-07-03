package com.bhtcnpm.website.model.entity.DocEntities;

import com.bhtcnpm.website.constant.business.GenericBusinessConstant;
import com.bhtcnpm.website.model.entity.UserWebsite;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity(name = "DocFileUpload")
@Table(name = "doc_file_upload")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocFileUpload {
    @Id
    @Column(columnDefinition = "uuid",
            nullable = false,
            unique = true)
    @Builder.Default
    private UUID id = UUID.randomUUID();

    @Column(nullable = false, updatable = false)
    private String remoteID;

    @Column(nullable = false)
    private Integer rank;

    @Column(nullable = false)
    private String fileName;

    //Stored as Byte.
    @Column(nullable = false)
    private Long fileSize;

    @Column(nullable = false, length = GenericBusinessConstant.URL_MAX_LENGTH)
    private String downloadURL;

    @Column(nullable = false, length = GenericBusinessConstant.URL_MAX_LENGTH)
    private String thumbnailURL;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    @Builder.Default
    private LocalDateTime createdDtm = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(nullable = false)
    private UserWebsite uploader;

    @ManyToOne
    @JoinColumn
    private Doc doc;

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof DocFileUpload)) return false;
        DocFileUpload other = (DocFileUpload) o;
        return Objects.equals(getId(), other.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
