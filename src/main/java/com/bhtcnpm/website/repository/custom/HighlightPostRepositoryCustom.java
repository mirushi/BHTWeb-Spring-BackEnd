package com.bhtcnpm.website.repository.custom;

import com.bhtcnpm.website.model.dto.Post.HighlightPostRequestDTO;
import com.bhtcnpm.website.model.dto.Post.HighlightPostUpdateDTO;
import com.bhtcnpm.website.model.entity.PostEntities.Post;
import com.bhtcnpm.website.model.entity.UserWebsite;

import java.util.List;

public interface HighlightPostRepositoryCustom {
    void createHighlightPost (Post post, UserWebsite user);
    void deleteHighlightPost (Long id);
    void stickToTop (Long id);
}
