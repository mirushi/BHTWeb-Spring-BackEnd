package com.bhtcnpm.website.service.Post.impl;

import com.bhtcnpm.website.model.dto.Post.*;
import com.bhtcnpm.website.model.entity.PostEntities.HighlightPost;
import com.bhtcnpm.website.model.entity.PostEntities.Post;
import com.bhtcnpm.website.model.entity.UserWebsite;
import com.bhtcnpm.website.repository.Post.HighlightPostRepository;
import com.bhtcnpm.website.repository.Post.PostRepository;
import com.bhtcnpm.website.repository.UserWebsiteRepository;
import com.bhtcnpm.website.security.util.SecurityUtils;
import com.bhtcnpm.website.service.Post.HighlightPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class HighlightPostServiceImpl implements HighlightPostService {

    private final HighlightPostRepository highlightPostRepository;

    private final UserWebsiteRepository userWebsiteRepository;

    private final PostRepository postRepository;

    private final HighlightPostMapper highlightPostMapper;

    @Override
    public List<HighlightPostDTO> getAllHighlightedPost() {
        List<HighlightPost> highlightPostList = highlightPostRepository.findAllByOrderByRankAsc();

        return highlightPostMapper.highlightPostListToHighlightPostDTOList(highlightPostList);
    }

    @Override
    public List<Long> getAllHighlightedPostIDs() {
        return highlightPostRepository.getAllHighlightedPostIDs();
    }

    @Override
    public void createHighlightPost(Long postID, Authentication authentication) {
        UUID userID = SecurityUtils.getUserIDOnNullThrowException(authentication);

        UserWebsite user = userWebsiteRepository.getOne(userID);
        Post post = postRepository.getOne(postID);

        highlightPostRepository.createHighlightPost(post, user);
    }

    public void deleteHighlightPost (Long postID) {
        highlightPostRepository.deleteHighlightPost(postID);
    }

    @Override
    public void stickToTop(Long postID) {
        highlightPostRepository.stickToTop(postID);
    }


}