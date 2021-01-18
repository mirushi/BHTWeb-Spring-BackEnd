package com.bhtcnpm.website.repository;

import com.bhtcnpm.website.model.entity.DocCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocCategoryRepository extends JpaRepository<DocCategory, Long> {
}
