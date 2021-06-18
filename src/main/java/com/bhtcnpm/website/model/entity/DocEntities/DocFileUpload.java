package com.bhtcnpm.website.model.entity.DocEntities;

import com.bhtcnpm.website.model.entity.UserWebsite;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.UUIDSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.util.UUID;

@Entity(name = "DocFileUpload")
@Table(name = "doc_file_upload")
@Data
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
}
