package com.bhtcnpm.website.service;

import com.bhtcnpm.website.model.dto.PostComment.PostCommentDTO;
import com.bhtcnpm.website.model.entity.PostEntities.PostComment;

import java.util.List;

public interface PostCommentService {

    List<PostCommentDTO> getPostCommentsByPostID (Long postID);

    List<PostCommentDTO> getChildComments (Long parentCommentID);

}
