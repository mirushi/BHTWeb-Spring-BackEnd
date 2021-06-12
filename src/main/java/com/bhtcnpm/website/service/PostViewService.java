package com.bhtcnpm.website.service;

import com.bhtcnpm.website.model.validator.dto.Post.PostID;
import org.springframework.security.core.Authentication;

import java.util.UUID;

public interface PostViewService {
    boolean addPostView (@PostID Long postID, UUID userID, String ipAddress);
    boolean addPostView (@PostID Long postID, Authentication authentication, String ipAddress);
}
