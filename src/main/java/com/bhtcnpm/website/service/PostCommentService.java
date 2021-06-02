package com.bhtcnpm.website.service;

import com.bhtcnpm.website.model.dto.PostComment.*;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface PostCommentService {
    PostCommentListDTO getPostCommentsByPostID (Long postID, Pageable pageable);

    List<PostCommentChildDTO> getChildComments (Long parentCommentID);

    PostCommentDTO postPostComment (PostCommentRequestDTO postCommentRequestDTO, Long postID, UUID authorID);

    PostCommentChildDTO postChildComment (PostCommentRequestDTO postCommentRequestDTO, Long parentCommentID, UUID authorID);

    PostCommentDTO putPostComment (PostCommentRequestDTO postCommentRequestDTO, Long commentID, UUID authorID);

    boolean deletePostComment (Long commentID);

    boolean createUserPostCommentLike (Long commentID, UUID userID);

    boolean deleteUserPostCommentLike (Long commentID, UUID userID);

    List<PostCommentStatisticDTO> getCommentStatistics (List<Long> commentIDs, UUID userID);
}
