package com.bhtcnpm.website.repository;

import com.bhtcnpm.website.model.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag findByContent (String content);
}
