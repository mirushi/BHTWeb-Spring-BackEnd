package com.bhtcnpm.website.service;

import com.bhtcnpm.website.model.validator.dto.Post.PostID;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import java.util.UUID;

public interface PostViewService {
    @PreAuthorize(value = "permitAll()")
    boolean addPostView (@PostID Long postID, UUID userID, String ipAddress);

    @PreAuthorize(value = "permitAll()")
    boolean addPostView (@PostID Long postID, Authentication authentication, String ipAddress);
}
