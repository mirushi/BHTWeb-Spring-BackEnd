package com.bhtcnpm.website.repository;

import com.bhtcnpm.website.model.entity.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag findByContentEquals(String content);

    @Query(value = "SELECT t " +
            "FROM Tag t " +
            "WHERE t.content LIKE %:contentLike% " +
            "ORDER BY "+ "(CASE WHEN EXISTS (SELECT 1 FROM t WHERE t.content = :contentExact) THEN TRUE ELSE FALSE END)" +" DESC, length(t.content)")
    List<Tag> getSimilarTags(Pageable pageable, String contentLike, String contentExact);
}
