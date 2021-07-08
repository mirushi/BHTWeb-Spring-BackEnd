package com.bhtcnpm.website.service.Post.impl;

import com.bhtcnpm.website.constant.business.PostComment.PostCommentActionAvailableConstant;
import com.bhtcnpm.website.constant.security.evaluator.permission.PostCommentActionPermissionRequest;
import com.bhtcnpm.website.model.dto.PostComment.*;
import com.bhtcnpm.website.model.entity.PostCommentEntities.PostComment;
import com.bhtcnpm.website.model.entity.PostCommentEntities.UserPostCommentLike;
import com.bhtcnpm.website.model.entity.PostCommentEntities.UserPostCommentLikeId;
import com.bhtcnpm.website.model.entity.enumeration.UserWebsite.ReputationType;
import com.bhtcnpm.website.repository.Post.PostCommentRepository;
import com.bhtcnpm.website.repository.Post.UserPostCommentLikeRepository;
import com.bhtcnpm.website.repository.UserWebsiteRepository;
import com.bhtcnpm.website.security.evaluator.PostComment.PostCommentPermissionEvaluator;
import com.bhtcnpm.website.security.util.SecurityUtils;
import com.bhtcnpm.website.service.Post.PostCommentService;
import com.bhtcnpm.website.service.UserWebsiteService;
import com.bhtcnpm.website.service.util.PaginatorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    private final UserWebsiteService userWebsiteService;

    private final PostCommentMapper postCommentMapper;

    private final PostCommentPermissionEvaluator postCommentPermissionEvaluator;

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
    public PostCommentDTO postPostComment(PostCommentRequestDTO postCommentRequestDTO, Long postID, Authentication authentication) {
        UUID authorID = SecurityUtils.getUserID(authentication);
        if (authorID == null) {
            throw new IllegalArgumentException("Cannot extract userID from authentication.");
        }

        if (postCommentRequestDTO == null) {
            return null;
        }

        PostComment postComment = postCommentMapper.postCommentDTOToPostComment(postCommentRequestDTO, postID, null, authorID,null);

        postComment = postCommentRepository.save(postComment);

        return postCommentMapper.postCommentToPostCommentDTOChildCommentOnly(postComment);
    }

    @Override
    public PostCommentChildDTO postChildComment(PostCommentRequestDTO postCommentRequestDTO, Long parentCommentID, Authentication authentication) {
        UUID authorID = SecurityUtils.getUserID(authentication);

        if (authorID == null) {
            throw new IllegalArgumentException("Cannot extract userID from authentication.");
        }

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
    public PostCommentDTO putPostComment(PostCommentRequestDTO postCommentRequestDTO, Long commentID, Authentication authentication) {
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
        postCommentRepository.deleteById(commentID);
        return true;
    }

    @Override
    public boolean createUserPostCommentLike(Long commentID, Authentication authentication) {
        UUID userID = SecurityUtils.getUserID(authentication);
        if (userID == null) {
            throw new IllegalArgumentException("Cannot extract user ID from authentication.");
        }

        UserPostCommentLikeId id =
                new UserPostCommentLikeId(userWebsiteRepository.getOne(userID), postCommentRepository.getOne(commentID));
        UserPostCommentLike entity = new UserPostCommentLike(id);
        userPostCommentLikeRepository.save(entity);

        userWebsiteService.addUserReputationScore(postCommentRepository.getOne(commentID).getAuthor().getId(), ReputationType.COMMENT_LIKED);

        return true;
    }

    @Override
    public boolean deleteUserPostCommentLike (Long commentID, Authentication authentication) {
        UUID userID = SecurityUtils.getUserID(authentication);

        if (userID == null) {
            throw new IllegalArgumentException("Cannot extract userID from authentication.");
        }

        UserPostCommentLikeId id =
                new UserPostCommentLikeId(userWebsiteRepository.getOne(userID), postCommentRepository.getOne(commentID));
        userPostCommentLikeRepository.deleteById(id);

        userWebsiteService.subtractUserReputationScore(postCommentRepository.getOne(commentID).getAuthor().getId(), ReputationType.COMMENT_LIKED);

        return true;
    }

    @Override
    public List<PostCommentStatisticDTO> getCommentStatistics (List<Long> commentIDs, Authentication authentication) {
        UUID userID = SecurityUtils.getUserID(authentication);

        List<PostCommentStatisticDTO> postCommentStatisticDTOs = postCommentRepository.getPostCommentStatisticDTOs(commentIDs, userID);

        return postCommentStatisticDTOs;
    }

    @Override
    public List<PostCommentAvailableActionDTO> getAvailablePostCommentAction(List<Long> postCommentIDs, Authentication authentication) {
        List<PostCommentAvailableActionDTO> postCommentAvailableActionDTOList = new ArrayList<>();

        for (Long postCommentID : postCommentIDs) {
            if (postCommentID == null) {
                continue;
            }

            PostCommentAvailableActionDTO postCommentAvailableActionDTO = new PostCommentAvailableActionDTO();
            postCommentAvailableActionDTO.setId(postCommentID);
            List<String> availableActionList = new ArrayList<>();

            if (postCommentPermissionEvaluator.hasPermission(authentication, postCommentID, PostCommentActionPermissionRequest.READ_PERMISSION)) {
                availableActionList.add(PostCommentActionAvailableConstant.READ_ACTION);
            }
            if (postCommentPermissionEvaluator.hasPermission(authentication, postCommentID, PostCommentActionPermissionRequest.UPDATE_PERMISSION)) {
                availableActionList.add(PostCommentActionAvailableConstant.UPDATE_ACTION);
            }
            if (postCommentPermissionEvaluator.hasPermission(authentication, postCommentID, PostCommentActionPermissionRequest.DELETE_PERMISSION)) {
                availableActionList.add(PostCommentActionAvailableConstant.DELETE_ACTION);
            }
            if (postCommentPermissionEvaluator.hasPermission(authentication, postCommentID, PostCommentActionPermissionRequest.LIKE_PERMISSION)) {
                availableActionList.add(PostCommentActionAvailableConstant.LIKE_ACTION);
            }
            if (postCommentPermissionEvaluator.hasPermission(authentication, postCommentID, PostCommentActionPermissionRequest.REPLY_PERMISSION)) {
                availableActionList.add(PostCommentActionAvailableConstant.REPLY_ACTION);
            }
            if (postCommentPermissionEvaluator.hasPermission(authentication, postCommentID, PostCommentActionPermissionRequest.REPORT_PERMISSION)) {
                availableActionList.add(PostCommentActionAvailableConstant.REPORT_ACTION);
            }

            postCommentAvailableActionDTO.setAvailableActions(availableActionList);
            postCommentAvailableActionDTOList.add(postCommentAvailableActionDTO);
        }

        return postCommentAvailableActionDTOList;
    }

}
