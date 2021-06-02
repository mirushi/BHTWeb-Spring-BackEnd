package com.bhtcnpm.website.repository.custom;

import com.bhtcnpm.website.model.dto.Post.PostSummaryDTO;
import com.bhtcnpm.website.model.dto.Post.PostSummaryListDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface UserPostSaveRepositoryCustom {
    PostSummaryListDTO findByUserPostSaveIdUserId (UUID userID, Pageable pageable);
}
