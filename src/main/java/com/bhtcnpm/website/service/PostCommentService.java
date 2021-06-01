package com.bhtcnpm.website.service;

import com.bhtcnpm.website.model.dto.PostComment.PostCommentDTO;
import com.bhtcnpm.website.model.dto.PostComment.PostCommentRequestDTO;
import com.bhtcnpm.website.model.entity.PostEntities.PostComment;

import java.util.List;
import java.util.UUID;

public interface PostCommentService {
    List<PostCommentDTO> getPostCommentsByPostID (Long postID);

    List<PostCommentDTO> getChildComments (Long parentCommentID);

    PostCommentDTO postPostComment (PostCommentRequestDTO postCommentRequestDTO, Long postID, UUID authorID);

    PostCommentDTO putPostComment (PostCommentRequestDTO postCommentRequestDTO, Long commentID, UUID authorID);

    boolean deletePostComment (Long commentID);
}
