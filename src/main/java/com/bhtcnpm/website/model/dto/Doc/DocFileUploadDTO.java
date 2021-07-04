package com.bhtcnpm.website.model.dto.Doc;

import com.bhtcnpm.website.model.entity.enumeration.DocFileUpload.DocFileUploadHostType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocFileUploadDTO {
    private UUID id;
    private String fileName;
    private Long fileSize;
    private String previewURL;
    private DocFileUploadHostType hostType;
}
