package com.bhtcnpm.website.service.impl;

import com.bhtcnpm.website.model.dto.PostComment.*;
import com.bhtcnpm.website.model.entity.PostCommentEntities.PostComment;
import com.bhtcnpm.website.model.entity.PostCommentEntities.UserPostCommentLike;
import com.bhtcnpm.website.model.entity.PostCommentEntities.UserPostCommentLikeId;
import com.bhtcnpm.website.repository.PostCommentRepository;
import com.bhtcnpm.website.repository.UserPostCommentLikeRepository;
import com.bhtcnpm.website.repository.UserWebsiteRepository;
import com.bhtcnpm.website.service.PostCommentService;
import com.bhtcnpm.website.service.util.PaginatorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class PostCommentServiceImpl implements PostCommentService {
    private static final int PAGE_SIZE = 10;

    private static final int CHILD_PAGE_SIZE = 100;

    private static final int DEFAULT_PRELOADED_COMMENT_COUNT = 3;

    private final PostCommentRepository postCommentRepository;

    private final UserPostCommentLikeRepository userPostCommentLikeRepository;

    private final UserWebsiteRepository userWebsiteRepository;

    private final PostCommentMapper postCommentMapper;

    @Override
    public PostCommentListDTO getPostCommentsByPostID(Long postID, Pageable pageable) {
        pageable = PaginatorUtils.getPageableWithNewPageSize(pageable, PAGE_SIZE);

        Page<PostCommentDTO> postCommentDTOs = postCommentRepository.getPostCommentDTOsParentOnly(postID, pageable);

        return postCommentMapper.postCommentPageToPostCommentListDTO(postCommentDTOs);
    }

    @Override
    public List<PostCommentChildDTO> getChildComments(Long parentCommentID, Pageable pageable) {
        pageable = PaginatorUtils.getPageableWithNewPageSize(pageable, CHILD_PAGE_SIZE);

        List<PostComment> queryResult = postCommentRepository.getPostCommentByParentCommentId(parentCommentID, pageable);

        List<PostCommentChildDTO> postCommentDTOs = postCommentMapper.postCommentListToPostCommentChildDTOList(queryResult);

        return postCommentDTOs;
    }

    @Override
    public PostCommentDTO postPostComment(PostCommentRequestDTO postCommentRequestDTO, Long postID, UUID authorID) {
        if (postCommentRequestDTO == null) {
            return null;
        }

        PostComment postComment = postCommentMapper.postCommentDTOToPostComment(postCommentRequestDTO, postID, null, authorID,null);

        postCommentRepository.save(postComment);

        return postCommentMapper.postCommentToPostCommentDTOChildCommentOnly(postComment);
    }

    @Override
    public PostCommentChildDTO postChildComment(PostCommentRequestDTO postCommentRequestDTO, Long parentCommentID, UUID authorID) {
        Optional<PostComment> parentComment = postCommentRepository.findById(parentCommentID);
        if (parentComment.isEmpty()) {
            throw new IllegalArgumentException("Parent comment not found.");
        }

        PostComment parentEntity = parentComment.get();
        //We don't want it to be nested too deep.
        if (parentEntity.getParentComment() != null) {
            throw new IllegalArgumentException("Cannot nest comment. Maximum comment depth is 2.");
        }

        PostComment childEntity = postCommentMapper.postCommentDTOToPostComment(postCommentRequestDTO, parentEntity.getPost().getId(), parentCommentID, authorID, null);
        postCommentRepository.save(childEntity);

        return postCommentMapper.postCommentToPostCommentChildDTO(childEntity);
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

        Long parentCommentId = null;
        if (postComment.getParentComment() != null) {
            parentCommentId = postComment.getParentComment().getId();
        }

        postComment = postCommentMapper.postCommentDTOToPostComment(
                postCommentRequestDTO, postComment.getPost().getId(),parentCommentId ,postComment.getAuthor().getId(), postComment);

        postComment = postCommentRepository.save(postComment);

        return postCommentMapper.postCommentToPostCommentDTOChildCommentOnly(postComment);
    }

    @Override
    public boolean deletePostComment(Long commentID) {
        //TODO: Please change this to soft delete. For now we'll be using permanent delete for testing purpose.
        postCommentRepository.deleteById(commentID);
        return true;
    }

    @Override
    public boolean createUserPostCommentLike(Long commentID, UUID userID) {
        UserPostCommentLikeId id =
                new UserPostCommentLikeId(userWebsiteRepository.getOne(userID), postCommentRepository.getOne(commentID));
        UserPostCommentLike entity = new UserPostCommentLike(id);
        userPostCommentLikeRepository.save(entity);
        return true;
    }

    @Override
    public boolean deleteUserPostCommentLike (Long commentID, UUID userID) {
        UserPostCommentLikeId id =
                new UserPostCommentLikeId(userWebsiteRepository.getOne(userID), postCommentRepository.getOne(commentID));
        userPostCommentLikeRepository.deleteById(id);
        return true;
    }

    @Override
    public List<PostCommentStatisticDTO> getCommentStatistics (List<Long> commentIDs, UUID userID) {
        List<PostCommentStatisticDTO> postCommentStatisticDTOs = postCommentRepository.getPostCommentStatisticDTOs(commentIDs, userID);

        return postCommentStatisticDTOs;
    }

}
