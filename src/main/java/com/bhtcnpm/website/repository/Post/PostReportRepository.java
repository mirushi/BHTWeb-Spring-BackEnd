package com.bhtcnpm.website.repository.Post;

import com.bhtcnpm.website.model.entity.PostEntities.Post;
import com.bhtcnpm.website.model.entity.PostEntities.PostReport;
import com.bhtcnpm.website.repository.custom.UserPostReportRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostReportRepository extends JpaRepository<PostReport, Long>, UserPostReportRepositoryCustom {

    @EntityGraph(value = "postReport.all")
    Page<PostReport> findAll (Pageable pageable);

    @EntityGraph(value = "postReport.all")
    Page<PostReport> findAllByResolvedTimeNotNull (Pageable pageable);

    @EntityGraph(value = "postReport.all")
    Page<PostReport> findAllByResolvedTimeIsNull (Pageable pageable);

    PostReport findByPost (Post post);

    PostReport findByPostAndActionTakenIsNull (Post post);
}
