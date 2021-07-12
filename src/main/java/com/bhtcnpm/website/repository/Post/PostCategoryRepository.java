package com.bhtcnpm.website.repository.Post;

import com.bhtcnpm.website.model.entity.PostEntities.PostCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostCategoryRepository extends JpaRepository<PostCategory, Long> {
}
