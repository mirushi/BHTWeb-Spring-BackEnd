package com.bhtcnpm.website.repository;

import com.bhtcnpm.website.model.entity.PostEntities.UserPostReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPostReportRepository extends JpaRepository<UserPostReport, Long> {

}
