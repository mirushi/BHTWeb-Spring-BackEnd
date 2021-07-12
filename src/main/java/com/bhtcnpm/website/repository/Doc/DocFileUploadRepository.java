package com.bhtcnpm.website.repository.Doc;

import com.bhtcnpm.website.model.entity.DocEntities.DocFileUpload;
import com.bhtcnpm.website.repository.Doc.custom.DocFileUploadRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DocFileUploadRepository extends JpaRepository<DocFileUpload, UUID>, DocFileUploadRepositoryCustom {
    @Query("SELECT dfu " +
            "FROM DocFileUpload dfu " +
            "WHERE (dfu.id IN :docFileUploadIDs) AND ((dfu.uploader.id = :uploaderID AND dfu.doc IS NULL) OR (dfu.doc.id = :docID))")
    List<DocFileUpload> filterDocFileUpload (List<UUID> docFileUploadIDs, Long docID, UUID uploaderID);
}
