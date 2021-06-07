package com.bhtcnpm.website.service.impl;

import com.bhtcnpm.website.constant.business.Post.PostBusinessConstant;
import com.bhtcnpm.website.model.dto.Post.*;
import com.bhtcnpm.website.model.dto.UserWebsite.SimpleKeycloakAccountWithEntity;
import com.bhtcnpm.website.model.entity.PostEntities.*;
import com.bhtcnpm.website.model.entity.UserWebsite;
import com.bhtcnpm.website.model.entity.enumeration.PostState.PostStateType;
import com.bhtcnpm.website.model.exception.IDNotFoundException;
import com.bhtcnpm.website.repository.PostRepository;
import com.bhtcnpm.website.repository.UserPostLikeRepository;
import com.bhtcnpm.website.repository.UserPostSaveRepository;
import com.bhtcnpm.website.repository.UserWebsiteRepository;
import com.bhtcnpm.website.security.predicate.Post.PostPredicateGenerator;
import com.bhtcnpm.website.security.util.SecurityUtils;
import com.bhtcnpm.website.service.PostService;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final UserPostLikeRepository userPostLikeRepository;

    private final UserPostSaveRepository userPostSaveRepository;

    private final UserWebsiteRepository userWebsiteRepository;

    private final PostMapper postMapper;

    private static final int PAGE_SIZE = 10;

    private static final int PAGE_SIZE_NEW_ACTIVITIES = 16;

    private static final int PAGE_SIZE_NEWEST = 16;

    @Override
    public List<PostStatisticDTO> getPostStatistic(List<Long> postIDs, Authentication authentication) {
        UUID userID = SecurityUtils.getUserID(authentication);
        List<PostStatisticDTO> postStatisticDTOS = postRepository.getPostStatisticDTOs(postIDs, userID);

        return postStatisticDTOS;
    }

    @Override
    public PostSummaryListDTO getPostSummary(Predicate predicate, Integer paginator, Authentication authentication) {
        Sort sort;
        BooleanExpression filterExpression = PostPredicateGenerator.getBooleanExpressionOnAuthentication(authentication);
        Pageable pageable = PageRequest.of(paginator, PAGE_SIZE);

        Page<Post> queryResults = postRepository.findAll(filterExpression.and(predicate), pageable);

        PostSummaryListDTO postSummaryListDTO = postMapper.postPageToPostSummaryListDTO(queryResults);

        return postSummaryListDTO;
    }

    @Override
    public PostDetailsDTO getPostDetails(Long id) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isPresent()) {
            return postMapper.postToPostDetailsDTO(post.get());
        }
        return null;
    }

    @Override
    public Boolean approvePost(Long postID, Authentication authentication) {
        int rowAffected = postRepository.setPostState(postID, PostStateType.APPROVED);
        return rowAffected == 1;
    }

    @Override
    public Boolean deletePostApproval(Long postID) {
        int rowAffected = postRepository.setPostState(postID, PostStateType.PENDING_APPROVAL);
        return rowAffected == 1;
    }

    @Override
    public Boolean createUserPostLike(Long postID, Authentication authentication) {
        UUID userID = SecurityUtils.getUserID(authentication);

        UserPostLikeId id = new UserPostLikeId();
        id.setPost(postRepository.getOne(postID));
        id.setUser(userWebsiteRepository.getOne(userID));
        UserPostLike userPostLike = new UserPostLike();
        userPostLike.setUserPostLikeId(id);

        userPostLikeRepository.save(userPostLike);

        return true;
    }

    @Override
    public Boolean deleteUserPostLike(Long postID, Authentication authentication) {
        UUID userID = SecurityUtils.getUserID(authentication);

        if (userID == null) {
            throw new IllegalArgumentException("Cannot extract userID from authentication.");
        }

        UserPostLikeId id = new UserPostLikeId();
        id.setPost(postRepository.getOne(postID));
        id.setUser(userWebsiteRepository.getOne(userID));

        try {
            userPostLikeRepository.deleteById(id);
        } catch (Exception exception) {
            return false;
        }

        return true;
    }

    @Override
    public PostDetailsDTO createPost(PostRequestDTO postRequestDTO, Authentication authentication) {
        UUID userID = SecurityUtils.getUserID(authentication);
        if (userID == null) {
            throw new IllegalArgumentException("Cannot create post. Not authenticated.");
        }
        Post post = postMapper.postRequestDTOToPost(postRequestDTO, userID, null);

        return postMapper.postToPostDetailsDTO(postRepository.save(post));
    }

    @Override
    public PostDetailsDTO editPost(PostRequestDTO postRequestDTO, Long postID, Authentication authentication) {
        UUID userID = SecurityUtils.getUserID(authentication);
        if (userID == null) {
            throw new AccessDeniedException("You must authenticated before using this API.");
        }

        Optional<Post> optionalPost = postRepository.findById(postID);
        if (!optionalPost.isPresent()) {
            return null;
        }

        Post post = optionalPost.get();

        post = postMapper.postRequestDTOToPost(postRequestDTO, userID, post);

        return postMapper.postToPostDetailsDTO(post);
    }

    @Override
    public Boolean deletePost(Long postID) {
        //TODO: Consider checking userID permission and saving who deleted the post.
        postRepository.deleteById(postID);
        return true;
    }

    @Override
    public Boolean rejectPost(Long postID, Authentication authentication) {
        int rowChanged = postRepository.setPostState(postID, PostStateType.REJECTED);

        if (rowChanged == 1) {
            return true;
        }
        return false;
    }

    @Override
    public Boolean rejectPostWithFeedback(Long postID, String feedback) {
        int rowChanged = postRepository.setPostStateAndFeedback(postID, PostStateType.PENDING_FIX, feedback);

        if (rowChanged == 1) {
            return true;
        }
        return false;
    }

    @Override
    public Boolean createSavedStatus(Long postID, Authentication authentication) {
        UUID userID = SecurityUtils.getUserID(authentication);
        if (userID == null) {
            throw new IllegalArgumentException("UserID not found. Cannot perform save.");
        }

        UserPostSaveId id = new UserPostSaveId();
        id.setPost(postRepository.getOne(postID));
        id.setUser(userWebsiteRepository.getOne(userID));
        UserPostSave userPostSave = new UserPostSave();
        userPostSave.setUserPostSaveId(id);

        userPostSaveRepository.save(userPostSave);

        return true;
    }

    @Override
    public Boolean deleteSavedStatus(Long postID, Authentication authentication) {
        UUID userID = SecurityUtils.getUserID(authentication);

        if (userID == null) {
            throw new IllegalArgumentException("Cannot extract userID from authentication.");
        }

        UserPostSaveId id = new UserPostSaveId();
        id.setPost(postRepository.getOne(postID));
        id.setUser(userWebsiteRepository.getOne(userID));

        userPostSaveRepository.deleteById(id);

        return true;
    }

    @Override
    public List<PostSummaryDTO> getPostWithActivityCategory() {
        Pageable pageable = PageRequest.of(0, PAGE_SIZE_NEW_ACTIVITIES);

        List<Post> queryResult = postRepository.findByCategoryNameOrderByPublishDtmDesc(pageable,"Hoạt động");

        return postMapper.postListToPostSummaryDTOs(queryResult);
    }

    @Override
    public List<PostSummaryDTO> getPostNewest() {
        Sort sort = Sort.by("publishDtm").descending();
        Pageable pageable = PageRequest.of(0, PAGE_SIZE_NEWEST, sort);

        Page<Post> queryResult = postRepository.findAll(pageable);

        return postMapper.postPageToPostSummaryDTOList(queryResult);
    }

    @Override
    public PostSummaryListDTO getPostBySearchTerm(String sortByPublishDtm, Integer page, String searchTerm, Long postCategoryID, Authentication authentication) {
        BooleanExpression filterExpression = PostPredicateGenerator.getBooleanExpressionOnAuthentication(authentication);

        PostSummaryListDTO postSummaryListDTO = postRepository.searchBySearchTerm(sortByPublishDtm, postCategoryID, page, PAGE_SIZE , searchTerm);
        return postSummaryListDTO;
    }

    @Override
    public PostSummaryWithStateListDTO getPostWithStateBySearchTerm (Predicate predicate, Pageable pageable) {
        //Reset PAGE_SIZE to predefined value.
        pageable = PageRequest.of(pageable.getPageNumber(), PAGE_SIZE, pageable.getSort());

        PostSummaryWithStateListDTO queryResult = postRepository.searchBySearchTermWithState(predicate, pageable);

        return queryResult;
    }

    @Override
    public PostDetailsWithStateListDTO getPostDetailsWithState(Predicate predicate, Pageable pageable, PostStateType postStateType) {
        //Reset PAGE_SIZE to predefined value.
        pageable = PageRequest.of(pageable.getPageNumber(), PAGE_SIZE, pageable.getSort());

        PostDetailsWithStateListDTO queryResult = postRepository.getPostDetailsListWithStateFilter(predicate, pageable, postStateType);

        return queryResult;
    }

    @Override
    public PostSummaryWithStateAndFeedbackListDTO getPostWithStateAndFeedback(Predicate predicate, Pageable pageable) {
        //Reset PAGE_SIZE to predefined value.
        pageable = PageRequest.of(pageable.getPageNumber(), PAGE_SIZE, pageable.getSort());

        PostSummaryWithStateAndFeedbackListDTO queryResult = postRepository.getPostSummaryStateFeedback(predicate, pageable);

        return queryResult;
    }

    @Override
    public List<PostSuggestionDTO> getRelatedPostSameAuthor(UUID authorID, Long postID, Integer page) throws IDNotFoundException, IOException {

        if (page == null) {
            page = 0;
        }

        Optional<Post> optEntity = postRepository.findById(postID);
        if (optEntity.isEmpty()) {
            throw new IDNotFoundException();
        }

        return postRepository.searchRelatedPost(authorID, null ,optEntity.get(), page ,PostBusinessConstant.RELATED_POST_MAX);
    }

    @Override
    public List<PostSuggestionDTO> getRelatedPostSameCategory(Long categoryID, Long postID, Integer page) throws IDNotFoundException, IOException {
        if (page == null) {
            page = 0;
        }

        Optional<Post> optEntity = postRepository.findById(postID);
        if (optEntity.isEmpty()) {
            throw new IDNotFoundException();
        }

        return postRepository.searchRelatedPost(null, categoryID ,optEntity.get(), page ,PostBusinessConstant.RELATED_POST_MAX);
    }

    @Override
    public PostSummaryListDTO getPostSavedByUserID(UUID userID, Pageable pageable) {
        //Reset PAGE_SIZE to predefined value.
        pageable = PageRequest.of(pageable.getPageNumber(), PAGE_SIZE, pageable.getSort());

        PostSummaryListDTO result = userPostSaveRepository.findByUserPostSaveIdUserId(userID, pageable);
        return result;
    }

    @Override
    public PostSummaryWithStateListDTO getManagementPost(String searchTerm, PostStateType postStateType, Integer page, String sortByPublishDtm, Long postCategoryID) {
        PostSummaryWithStateListDTO dto = postRepository.getManagementPost(sortByPublishDtm,
                postCategoryID,
                page,
                PAGE_SIZE,
                searchTerm,
                postStateType);
        return dto;
    }

}
