package com.bhtcnpm.website.repository.Doc;

import com.bhtcnpm.website.model.entity.DocEntities.DocFileUpload;
import com.bhtcnpm.website.repository.Doc.custom.DocFileUploadRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DocFileUploadRepository extends JpaRepository<DocFileUpload, UUID>, DocFileUploadRepositoryCustom {
    List<DocFileUpload> findAllByIdInAndUploaderId (List<UUID> docFileUploadIDs, UUID uploaderID);
}
