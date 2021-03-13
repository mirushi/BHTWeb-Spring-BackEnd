package com.bhtcnpm.website.model.entity.DocEntities;

import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.util.UUID;

@Entity(name = "DocFileUpload")
@Table(name = "doc_file_upload")
@Data
public class DocFileUpload {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "doc_file_upload_sequence"
    )
    @SequenceGenerator(
            name = "doc_file_upload_sequence",
            sequenceName = "doc_file_upload_sequence"
    )
    private Long id;

    @Column(columnDefinition = "BINARY(16)",
            nullable = false,
            unique = true)
    private UUID code;

    @Column(nullable = false)
    private String fileName;

    //Stored as KB.
    @Column(nullable = false)
    private Long fileSize;

    private String downloadURL;

    @PrePersist
    public void initializeUUID () {
        if (code == null) {
            code = UUID.randomUUID();
        }
    }
}
