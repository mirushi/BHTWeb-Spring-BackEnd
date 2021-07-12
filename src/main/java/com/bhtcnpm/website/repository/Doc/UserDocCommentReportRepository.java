package com.bhtcnpm.website.repository.Doc;

import com.bhtcnpm.website.model.entity.DocCommentEntities.report.UserDocCommentReport;
import com.bhtcnpm.website.model.entity.DocCommentEntities.report.UserDocCommentReportId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDocCommentReportRepository extends JpaRepository<UserDocCommentReport, UserDocCommentReportId> {
}
