package com.bhtcnpm.website.repository.Post;

import com.bhtcnpm.website.model.entity.PostEntities.UserPostReport;
import com.bhtcnpm.website.model.entity.PostEntities.UserPostReportId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPostReportRepository extends JpaRepository<UserPostReport, UserPostReportId> {
    UserPostReport findByUserPostReportId (UserPostReportId id);
}
