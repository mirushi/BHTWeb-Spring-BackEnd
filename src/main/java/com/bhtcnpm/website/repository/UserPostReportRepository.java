package com.bhtcnpm.website.repository;

import com.bhtcnpm.website.model.entity.PostEntities.PostReport;
import com.bhtcnpm.website.repository.custom.UserPostReportRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPostReportRepository extends JpaRepository<PostReport, Long>, UserPostReportRepositoryCustom {
    Page<PostReport> findAllByResolvedTimeNotNull (Pageable pageable);
    Page<PostReport> findAllByResolvedTimeIsNull (Pageable pageable);
}
