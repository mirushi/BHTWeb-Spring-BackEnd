package com.bhtcnpm.website.repository.Post;

import com.bhtcnpm.website.model.entity.PostCommentEntities.PostComment;
import com.bhtcnpm.website.model.entity.PostCommentEntities.PostCommentReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostCommentReportRepository extends JpaRepository<PostCommentReport, Long> {
    @EntityGraph(value = "postCommentReport.all")
    Page<PostCommentReport> findAll (Pageable pageable);

    @EntityGraph(value = "postCommentReport.all")
    Page<PostCommentReport> findAllByResolvedTimeNotNull (Pageable pageable);

    @EntityGraph(value = "postCommentReport.all")
    Page<PostCommentReport> findAllByResolvedTimeIsNull(Pageable pageable);

    PostCommentReport findByPostComment (PostComment postComment);

    PostCommentReport findByPostCommentAndActionTakenIsNull (PostComment postComment);
}
