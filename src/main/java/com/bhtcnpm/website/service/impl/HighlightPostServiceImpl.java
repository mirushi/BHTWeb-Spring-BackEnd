package com.bhtcnpm.website.service.impl;

import com.bhtcnpm.website.model.dto.Post.*;
import com.bhtcnpm.website.model.entity.PostEntities.HighlightPost;
import com.bhtcnpm.website.model.entity.PostEntities.Post;
import com.bhtcnpm.website.model.entity.UserWebsite;
import com.bhtcnpm.website.repository.HighlightPostRepository;
import com.bhtcnpm.website.repository.PostRepository;
import com.bhtcnpm.website.repository.UserWebsiteRepository;
import com.bhtcnpm.website.service.HighlightPostService;
import lombok.RequiredArgsConstructor;
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
    public void createHighlightPost(HighlightPostRequestDTO dto, UUID userID) {
        UserWebsite user = userWebsiteRepository.getOne(userID);
        Post post = postRepository.getOne(dto.getId());

        highlightPostRepository.createHighlightPost(post, user);
    }

    public void deleteHighlightPost (Long postID) {
        highlightPostRepository.deleteHighlightPost(postID);
    }

    @Override
    public void stickToTop(Long id) {
        highlightPostRepository.stickToTop(id);
    }


}