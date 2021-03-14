package com.bhtcnpm.website.model.entity;

import com.bhtcnpm.website.model.entity.DocEntities.DocFileUpload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocFileUploadRepository extends JpaRepository<DocFileUpload, Long> {
}
