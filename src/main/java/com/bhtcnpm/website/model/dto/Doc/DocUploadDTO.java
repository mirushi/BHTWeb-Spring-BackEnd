package com.bhtcnpm.website.model.dto.Doc;

import lombok.Data;

@Data
public class DocUploadDTO {
    private String fileName;
    private String fileDownloadURL;
    private String fileType;
    private String size;
}
