package com.bhtcnpm.website.repository.Doc;

import com.bhtcnpm.website.model.entity.DocEntities.DocDownload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface DocDownloadRepository extends JpaRepository<DocDownload, Long> {
    @Query("SELECT CASE WHEN (COUNT (dd) > 0) THEN TRUE ELSE FALSE END " +
            "FROM DocDownload dd " +
            "WHERE dd.docFileUpload.id = :docFileUploadID AND (dd.user.id = :userID OR dd.ipAddress = :ipAddress AND dd.user.id IS NULL)")
    boolean existsByDocIdAndUserIdOrIpAddress (UUID docFileUploadID, UUID userID, String ipAddress);
}
