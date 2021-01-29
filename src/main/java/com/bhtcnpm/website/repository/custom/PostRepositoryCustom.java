package com.bhtcnpm.website.repository.custom;

import com.bhtcnpm.website.model.dto.Post.PostSummaryDTO;
import com.bhtcnpm.website.model.dto.Post.PostSummaryListDTO;
import com.bhtcnpm.website.model.entity.PostEntities.Post;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostRepositoryCustom {
    PostSummaryListDTO searchBySearchTerm (Predicate predicate, Pageable pageable, String searchTerm);
}
