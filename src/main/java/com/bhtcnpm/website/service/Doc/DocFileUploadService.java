package com.bhtcnpm.website.service.Doc;

import com.bhtcnpm.website.model.entity.DocEntities.DocFileUpload;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.UUID;

public interface DocFileUploadService {
    List<DocFileUpload> getFileUploadOwnerOnly (List<UUID> docFileIDList, Authentication authentication);
}
