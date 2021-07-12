package com.bhtcnpm.website.repository.Post;

import com.bhtcnpm.website.model.entity.PostCommentEntities.UserPostCommentReport;
import com.bhtcnpm.website.model.entity.PostCommentEntities.UserPostCommentReportId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPostCommentReportRepository extends JpaRepository<UserPostCommentReport, UserPostCommentReportId> {
}
