package com.bhtcnpm.website.repository.Doc;

import com.bhtcnpm.website.model.entity.DocEntities.DocCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocCategoryRepository extends JpaRepository<DocCategory, Long> {
}
