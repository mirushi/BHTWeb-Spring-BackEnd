package com.bhtcnpm.website.repository.custom;

import com.bhtcnpm.website.model.dto.Post.PostSummaryDTO;
import com.bhtcnpm.website.model.dto.Post.PostSummaryListDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserPostSaveRepositoryCustom {
    PostSummaryListDTO findByUserPostSaveIdUserId (Long userID, Pageable pageable);
}
