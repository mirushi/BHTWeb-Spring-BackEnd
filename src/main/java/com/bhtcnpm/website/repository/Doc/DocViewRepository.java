package com.bhtcnpm.website.repository.Doc;

import com.bhtcnpm.website.model.entity.DocEntities.DocView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface DocViewRepository extends JpaRepository<DocView, Long> {
    @Query("SELECT CASE WHEN (COUNT(dv) > 0) THEN TRUE ELSE FALSE END " +
            "FROM DocView dv " +
            "WHERE dv.doc.id = :docID AND ( dv.user.id = :userID OR dv.ipAddress = :ipAddress AND dv.user.id IS NULL)")
    boolean existsByDocIdAndUserIdOrIpAddress (Long docID, UUID userID, String ipAddress);
}
