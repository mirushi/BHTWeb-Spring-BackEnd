package com.bhtcnpm.website.repository.Doc;

import com.bhtcnpm.website.model.entity.DocCommentEntities.DocComment;
import com.bhtcnpm.website.model.entity.DocCommentEntities.report.DocCommentReport;
import com.bhtcnpm.website.service.Doc.DocCommentReportService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocCommentReportRepository extends JpaRepository<DocCommentReport, Long> {
    @EntityGraph(value = "docCommentReport.all")
    Page<DocCommentReport> findAll (Pageable pageable);

    @EntityGraph(value = "docCommentReport.all")
    Page<DocCommentReport> findAllByResolvedTimeNotNull (Pageable pageable);

    @EntityGraph(value = "docCommentReport.all")
    Page<DocCommentReport> findAllByResolvedTimeIsNull (Pageable pageable);

    DocCommentReport findByDocComment (DocComment docComment);

    DocCommentReport findByDocCommentAndActionTakenIsNull (DocComment docComment);
}
