package com.bhtcnpm.website.repository;

import com.bhtcnpm.website.model.entity.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag findByContentEquals(String content);
    List<Tag> findByContentEqualsOrContentContainingIgnoreCase (Pageable pageable, String contentEquals, String contentContaining);
}
