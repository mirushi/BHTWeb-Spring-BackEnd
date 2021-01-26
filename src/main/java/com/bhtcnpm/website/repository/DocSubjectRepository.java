package com.bhtcnpm.website.repository;

import com.bhtcnpm.website.model.entity.DocEntities.DocSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocSubjectRepository extends JpaRepository<DocSubject, Long> {
}
