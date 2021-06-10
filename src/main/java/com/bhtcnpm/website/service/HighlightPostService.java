package com.bhtcnpm.website.service;

import com.bhtcnpm.website.model.dto.Post.HighlightPostDTO;
import com.bhtcnpm.website.model.dto.Post.HighlightPostRequestDTO;
import com.bhtcnpm.website.model.dto.Post.HighlightPostUpdateDTO;
import com.bhtcnpm.website.model.dto.Post.HighlightPostUpdateListDTO;
import com.bhtcnpm.website.model.validator.dto.Post.PostID;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.UUID;

public interface HighlightPostService {
    @PreAuthorize(value = "permitAll()")
    List<HighlightPostDTO> getAllHighlightedPost ();
    @PreAuthorize(value = "permitAll()")
    List<Long> getAllHighlightedPostIDs ();
    @PreAuthorize(value = "hasPermission(#postID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).POST_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.HighlightPostPermissionRequest).HIGHLIGHT_POST_MANAGE)")
    void createHighlightPost (@PostID Long postID, Authentication authentication);

    @PreAuthorize(value = "hasPermission(#postID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).POST_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.HighlightPostPermissionRequest).HIGHLIGHT_POST_MANAGE)")
    void deleteHighlightPost (@PostID Long postID);

    @PreAuthorize(value = "hasPermission(#postID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).POST_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.HighlightPostPermissionRequest).HIGHLIGHT_POST_MANAGE)")
    void stickToTop (@PostID Long postID);
}
