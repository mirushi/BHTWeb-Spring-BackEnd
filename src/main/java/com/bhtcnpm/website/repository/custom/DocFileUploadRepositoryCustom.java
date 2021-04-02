package com.bhtcnpm.website.repository.custom;

import com.bhtcnpm.website.model.entity.DocEntities.DocFileUpload;

import java.util.UUID;

public interface DocFileUploadRepositoryCustom {
    DocFileUpload findByCode (UUID uuid);
}
