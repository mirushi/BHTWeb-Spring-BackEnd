package com.bhtcnpm.website.repository.Doc;

import com.bhtcnpm.website.model.entity.DocEntities.Doc;
import com.bhtcnpm.website.model.entity.DocEntities.report.DocReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocReportRepository extends JpaRepository<DocReport, Long> {
    @EntityGraph(value = "docReport.all")
    Page<DocReport> findAll (Pageable pageable);

    @EntityGraph(value = "docReport.all")
    Page<DocReport> findAllByResolvedTimeNotNull (Pageable pageable);

    @EntityGraph(value = "docReport.all")
    Page<DocReport> findAllByResolvedTimeIsNull (Pageable pageable);

    DocReport findByDoc (Doc doc);

    DocReport findByDocAndActionTakenIsNull (Doc doc);
}
