package com.bhtcnpm.website.service;

import com.bhtcnpm.website.model.dto.Post.HighlightPostDTO;
import com.bhtcnpm.website.model.dto.Post.HighlightPostRequestDTO;
import com.bhtcnpm.website.model.dto.Post.HighlightPostUpdateDTO;
import com.bhtcnpm.website.model.dto.Post.HighlightPostUpdateListDTO;

import java.util.List;

public interface HighlightPostService {
    List<HighlightPostDTO> getAllHighlightedPost ();
    List<Long> getAllHighlightedPostIDs ();
    void createHighlightPost (HighlightPostRequestDTO highlightPostRequestDTO, Long userID);
    void deleteHighlightPost (Long postID);
    void stickToTop (Long id);
}