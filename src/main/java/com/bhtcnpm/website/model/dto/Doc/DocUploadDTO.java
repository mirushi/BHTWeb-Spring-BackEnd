package com.bhtcnpm.website.model.dto.Doc;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class DocUploadDTO {
    private UUID id;
    private String fileName;
    private Long fileSize;
}
