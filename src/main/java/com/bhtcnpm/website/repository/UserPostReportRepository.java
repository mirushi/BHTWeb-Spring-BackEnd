package com.bhtcnpm.website.repository;

import com.bhtcnpm.website.model.entity.PostEntities.UserPostReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface UserPostReportRepository extends JpaRepository<UserPostReport, Long> {
    Page<UserPostReport> findAllByResolvedTimeNotNull (Pageable pageable);
    Page<UserPostReport> findAllByResolvedTimeIsNull (Pageable pageable);
}
