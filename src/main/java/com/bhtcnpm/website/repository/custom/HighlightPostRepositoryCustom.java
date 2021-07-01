package com.bhtcnpm.website.repository.custom;

import com.bhtcnpm.website.model.entity.PostEntities.Post;
import com.bhtcnpm.website.model.entity.UserWebsite;

public interface HighlightPostRepositoryCustom {
    void createHighlightPost (Post post, UserWebsite user);
    void deleteHighlightPost (Long id);
    void stickToTop (Long id);
}
