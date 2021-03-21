package com.bhtcnpm.website.model.entity;

import com.bhtcnpm.website.model.entity.DocEntities.DocFileUpload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DocFileUploadRepository extends JpaRepository<DocFileUpload, Long> {
    DocFileUpload findByCode (UUID code);
}
