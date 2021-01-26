package com.bhtcnpm.website.repository;

import com.bhtcnpm.website.model.entity.DocComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocCommentRepository extends JpaRepository<DocComment, Long> {
    Page<DocComment> findAllByDocId (Pageable pageable, Long docID);
}
