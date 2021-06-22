package com.bhtcnpm.website.repository;

import com.bhtcnpm.website.model.entity.PostEntities.HighlightPost;
import com.bhtcnpm.website.model.entity.PostEntities.Post;
import com.bhtcnpm.website.repository.custom.HighlightPostRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HighlightPostRepository extends JpaRepository<HighlightPost, Long>, HighlightPostRepositoryCustom {
    @Query("SELECT hlp.highlightPostId.post.id FROM HighlightPost hlp")
    List<Long> getAllHighlightedPostIDs ();

    List<HighlightPost> findAllByOrderByRankAsc ();

    Optional<HighlightPost> findByHighlightPostIdPost (Post post);
}
