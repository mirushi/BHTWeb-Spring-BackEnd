package com.bhtcnpm.website.repository;

import com.bhtcnpm.website.model.dto.Doc.DocDownloadInfoDTO;
import com.bhtcnpm.website.model.entity.DocEntities.DocFileUpload;
import com.bhtcnpm.website.repository.custom.DocFileUploadRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DocFileUploadRepository extends JpaRepository<DocFileUpload, UUID>, DocFileUploadRepositoryCustom {
}
