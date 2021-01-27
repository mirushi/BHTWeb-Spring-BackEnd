package com.bhtcnpm.website.service.impl;

import com.bhtcnpm.website.model.dto.PostComment.PostCommentDTO;
import com.bhtcnpm.website.model.dto.PostComment.PostCommentMapper;
import com.bhtcnpm.website.model.dto.PostComment.PostCommentRequestDTO;
import com.bhtcnpm.website.model.entity.PostEntities.PostComment;
import com.bhtcnpm.website.repository.PostCommentRepository;
import com.bhtcnpm.website.service.PostCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PostCommentServiceImpl implements PostCommentService {

    private final PostCommentRepository postCommentRepository;

    private final PostCommentMapper postCommentMapper;

    @Override
    public List<PostCommentDTO> getPostCommentsByPostID(Long postID) {
        List<PostCommentDTO> postCommentDTOs = postCommentRepository.getPostCommentDTOsParentOnly(postID);

        return postCommentDTOs;
    }

    @Override
    public List<PostCommentDTO> getChildComments(Long parentCommentID) {
        List<PostComment> queryResult = postCommentRepository.getPostCommentByParentCommentId(parentCommentID);

        List<PostCommentDTO> postCommentDTOs = postCommentMapper.postCommentListToPostCommentDTOListChildCommentOnly(queryResult);

        return postCommentDTOs;
    }

    @Override
    public PostCommentDTO postPostComment(PostCommentRequestDTO postCommentRequestDTO, Long postID, Long authorID) {

        if (postCommentRequestDTO == null) {
            return null;
        }

        PostComment postComment = postCommentMapper.postCommentDTOToPostComment(postCommentRequestDTO, postID, authorID,null);

        postCommentRepository.save(postComment);

        return postCommentMapper.postCommentToPostCommentDTOChildCommentOnly(postComment);
    }
}
