package com.bhtcnpm.website.service;

import com.bhtcnpm.website.model.dto.PostComment.PostCommentChildDTO;
import com.bhtcnpm.website.model.dto.PostComment.PostCommentDTO;
import com.bhtcnpm.website.model.dto.PostComment.PostCommentListDTO;
import com.bhtcnpm.website.model.dto.PostComment.PostCommentRequestDTO;
import com.bhtcnpm.website.model.entity.PostEntities.PostComment;
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
}
