package com.bhtcnpm.website.service.impl;

import com.bhtcnpm.website.model.dto.PostComment.PostCommentDTO;
import com.bhtcnpm.website.model.dto.PostComment.PostCommentMapper;
import com.bhtcnpm.website.model.dto.PostComment.PostCommentRequestDTO;
import com.bhtcnpm.website.model.entity.PostEntities.PostComment;
import com.bhtcnpm.website.repository.PostCommentRepository;
import com.bhtcnpm.website.service.PostCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    public PostCommentDTO postPostComment(PostCommentRequestDTO postCommentRequestDTO, Long postID, UUID authorID) {

        if (postCommentRequestDTO == null) {
            return null;
        }

        PostComment postComment = postCommentMapper.postCommentDTOToPostComment(postCommentRequestDTO, postID, authorID,null);

        postCommentRepository.save(postComment);

        return postCommentMapper.postCommentToPostCommentDTOChildCommentOnly(postComment);
    }

    @Override
    public PostCommentDTO putPostComment(PostCommentRequestDTO postCommentRequestDTO, Long commentID, UUID authorID) {

        if (postCommentRequestDTO == null) {
            return null;
        }

        Optional<PostComment> optionalPostComment = postCommentRepository.findById(commentID);
        if (!optionalPostComment.isPresent()) {
            return null;
        }

        PostComment postComment = optionalPostComment.get();

        postComment = postCommentMapper.postCommentDTOToPostComment(
                postCommentRequestDTO, postComment.getPost().getId(), postComment.getAuthor().getId(), postComment);

        postComment = postCommentRepository.save(postComment);

        return postCommentMapper.postCommentToPostCommentDTOChildCommentOnly(postComment);
    }

    @Override
    public boolean deletePostComment(Long commentID) {
        //TODO: Please change this to soft delete. For now we'll be using permanent delete for testing purpose.
        postCommentRepository.deleteById(commentID);
        return true;
    }
}
