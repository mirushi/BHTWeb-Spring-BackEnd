package com.bhtcnpm.website.repository.Doc;

import com.bhtcnpm.website.model.entity.DocEntities.report.UserDocReport;
import com.bhtcnpm.website.model.entity.DocEntities.report.UserDocReportId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDocReportRepository extends JpaRepository<UserDocReport, UserDocReportId> {
    UserDocReport findByUserDocReportId (UserDocReportId id);
}
