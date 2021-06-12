package com.bhtcnpm.website.service.impl;

import com.bhtcnpm.website.model.entity.PostEntities.PostView;
import com.bhtcnpm.website.repository.PostRepository;
import com.bhtcnpm.website.repository.PostViewRepository;
import com.bhtcnpm.website.repository.UserWebsiteRepository;
import com.bhtcnpm.website.security.util.SecurityUtils;
import com.bhtcnpm.website.service.PostViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class PostViewServiceImpl implements PostViewService {

    private final PostViewRepository postViewRepository;

    private final PostRepository postRepository;

    private final UserWebsiteRepository uwRepository;

    @Override
    public boolean addPostView(Long postID, UUID userID, String ipAddress) {
        boolean isUserViewedPost = postViewRepository.existsByPostIdAndUserIdOrIpAddress(postID, userID, ipAddress);
        if (isUserViewedPost) {
            return false;
        }

        PostView postView = new PostView();
        postView.setPost(postRepository.getOne(postID));
        postView.setUser(uwRepository.getOne(userID));
        postView.setIpAddress(ipAddress);

        postViewRepository.save(postView);

        return true;
    }

    @Override
    public boolean addPostView(Long postID, Authentication authentication, String ipAddress) {
        UUID userID = SecurityUtils.getUserID(authentication);

        return addPostView(postID, userID, ipAddress);
    }
}
