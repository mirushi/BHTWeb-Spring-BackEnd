package com.bhtcnpm.website.model.entity.DocEntities;

import com.bhtcnpm.website.model.entity.UserWebsite;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.UUIDSerializer;
import lombok.*;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
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
    @Column(columnDefinition = "BINARY(16)",
            nullable = false,
            unique = true)
    private UUID id;

    @Column(nullable = false)
    private String fileName;

    //Stored as Byte.
    @Column(nullable = false)
    private Long fileSize;

    @Column(nullable = false)
    private String downloadURL;

    @Column(nullable = false)
    private Long downloadCount;

    @Column(nullable = false)
    private String thumbnailURL;

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
