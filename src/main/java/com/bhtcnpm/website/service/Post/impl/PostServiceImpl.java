package com.bhtcnpm.website.service.Post.impl;

import com.bhtcnpm.website.constant.business.Post.PostActionAvailableConstant;
import com.bhtcnpm.website.constant.business.Post.PostBusinessConstant;
import com.bhtcnpm.website.constant.domain.Post.PostBusinessState;
import com.bhtcnpm.website.constant.security.evaluator.permission.HighlightPostPermissionRequest;
import com.bhtcnpm.website.constant.security.evaluator.permission.PostActionPermissionRequest;
import com.bhtcnpm.website.model.dto.Post.*;
import com.bhtcnpm.website.model.entity.ExerciseEntities.Exercise;
import com.bhtcnpm.website.model.entity.PostEntities.*;
import com.bhtcnpm.website.model.entity.Tag;
import com.bhtcnpm.website.model.entity.enumeration.PostState.PostStateType;
import com.bhtcnpm.website.model.exception.IDNotFoundException;
import com.bhtcnpm.website.repository.*;
import com.bhtcnpm.website.repository.Exercise.ExerciseRepository;
import com.bhtcnpm.website.repository.Post.PostRepository;
import com.bhtcnpm.website.repository.Post.UserPostLikeRepository;
import com.bhtcnpm.website.repository.Post.UserPostSaveRepository;
import com.bhtcnpm.website.security.evaluator.Post.PostPermissionEvaluator;
import com.bhtcnpm.website.security.predicate.Post.PostPredicateGenerator;
import com.bhtcnpm.website.security.util.SecurityUtils;
import com.bhtcnpm.website.service.Post.PostService;
import com.bhtcnpm.website.service.Post.PostViewService;
import com.bhtcnpm.website.service.util.PaginatorUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
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

    private final TagRepository tagRepository;

    private final PostMapper postMapper;

    private final PostPermissionEvaluator postPermissionEvaluator;

    private final PostViewService postViewService;

    private final ExerciseRepository exerciseRepository;

    private static final int PAGE_SIZE = 10;

    private static final int PAGE_SIZE_NEW_ACTIVITIES = 16;

    private static final int PAGE_SIZE_NEWEST = 16;

    private static final int PAGE_SIZE_TRENDING_HOME = 3;

    @Override
    public List<PostStatisticDTO> getPostStatistic(List<Long> postIDs, Authentication authentication) {
        UUID userID = SecurityUtils.getUserID(authentication);
        List<PostStatisticDTO> postStatisticDTOS = postRepository.getPostStatisticDTOs(postIDs, userID);

        return postStatisticDTOS;
    }

    @Override
    public PostSummaryListDTO getPostSummary(Predicate predicate, Pageable pageable, boolean mostLiked, boolean mostViewed, Authentication authentication) {
        //Only allow sort by one field at a time.
        if (mostLiked && mostViewed) {
            throw new IllegalArgumentException("Only use mostLiked or mostViewed one at a time.");
        }

        Sort sort;

        BooleanExpression authorizationExpression = PostPredicateGenerator.getBooleanExpressionOnAuthentication(authentication);
        BooleanExpression postAllowedBusinessState = PostPredicateGenerator.getBooleanExpressionOnBusinessState(PostBusinessState.PUBLIC);
        BooleanExpression finalPredicate = authorizationExpression.and(postAllowedBusinessState).and(predicate);

        pageable = PaginatorUtils.getPageableWithNewPageSize(pageable, PAGE_SIZE);

        PostSummaryListDTO finalResult;

        if (mostLiked) {
            Page<PostSummaryDTO> postSummaryDTOPage = postRepository.getPostOrderByLikeCountDESC(finalPredicate, pageable);
            finalResult = postMapper.postSummaryPageToPostSummaryListDTO(postSummaryDTOPage);
        }
        else if (mostViewed) {
            Page<PostSummaryDTO> postSummaryDTOPage = postRepository.getPostOrderByViewCountDESC(finalPredicate, pageable);
            finalResult = postMapper.postSummaryPageToPostSummaryListDTO(postSummaryDTOPage);
        } else {
            Page<Post> postResult = postRepository.findAll(finalPredicate, pageable);
            finalResult = postMapper.postPageToPostSummaryListDTO(postResult);
        }

        return finalResult;
    }

    @Override
    public PostDetailsDTO getPostDetails(Long id) {
        Optional<Post> post = postRepository.findByIDWithTags(id);
        if (post.isPresent()) {
            return postMapper.postToPostDetailsDTO(post.get());
        }
        return null;
    }

    @Override
    public Boolean approvePost(Long postID, Authentication authentication) {
        int rowAffected = postRepository.setPostState(postID, PostStateType.APPROVED);
        if (rowAffected == 1) {
            postRepository.indexPost(postID);
            return true;
        }
        return false;
    }

    @Override
    public Boolean deletePostApproval(Long postID) {
        int rowAffected = postRepository.setPostState(postID, PostStateType.PENDING_APPROVAL);
        if (rowAffected == 1) {
            postRepository.indexPost(postID);
            return true;
        }
        return false;
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
        post = postRepository.save(post);

        return postMapper.postToPostDetailsDTO(post);
    }

    @Override
    public PostDetailsDTO editPost(PostRequestDTO postRequestDTO, Long postID, Authentication authentication) {
        UUID userID = SecurityUtils.getUserID(authentication);
        if (userID == null) {
            throw new AccessDeniedException("You must authenticated before using this API.");
        }

        Optional<Post> optionalPost = postRepository.findByIDWithTags(postID);
        if (!optionalPost.isPresent()) {
            return null;
        }

        Post post = optionalPost.get();

        post = postMapper.postRequestDTOToPost(postRequestDTO, userID, post);

        return postMapper.postToPostDetailsDTO(post);
    }

    @Override
    public Boolean deletePost(Long postID, Authentication authentication) {
//        UUID userID = SecurityUtils.getUserID(authentication);
//        if (userID == null) {
//            throw new IllegalArgumentException("Cannot extract userID from authentication.");
//        }
//
//        Optional<Post> post = postRepository.findById(postID);
//        if (post.isEmpty()) {
//            throw new IDNotFoundException();
//        }
//        Post entity = post.get();
//
//        entity.setDeletedDate(LocalDateTime.now());
//        entity.setDeletedBy(userWebsiteRepository.getOne(userID));
//
//        postRepository.removeIndexPost(entity);

        postRepository.deleteById(postID);

        return true;
    }

    @Override
    public Boolean rejectPost(Long postID, Authentication authentication) {
        int rowChanged = postRepository.setPostState(postID, PostStateType.REJECTED);

        if (rowChanged == 1) {
            postRepository.indexPost(postID);
            return true;
        }
        return false;
    }

    @Override
    public Boolean rejectPostWithFeedback(Long postID, String feedback) {
        int rowChanged = postRepository.setPostStateAndFeedback(postID, PostStateType.PENDING_FIX, feedback);

        if (rowChanged == 1) {
            postRepository.indexPost(postID);
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
        UUID userID = SecurityUtils.getUserIDOnNullThrowException(authentication);

        UserPostSaveId id = new UserPostSaveId();
        id.setPost(postRepository.getOne(postID));
        id.setUser(userWebsiteRepository.getOne(userID));

        userPostSaveRepository.deleteById(id);

        return true;
    }

    @Override
    public List<PostSummaryDTO> getPostWithActivityCategory() {
        Pageable pageable = PageRequest.of(0, PAGE_SIZE_NEW_ACTIVITIES);

        BooleanExpression postBusinessState = PostPredicateGenerator.getBooleanExpressionOnBusinessState(PostBusinessState.PUBLIC);

        List<Post> queryResult = postRepository.findByCategoryNameOrderByPublishDtmDesc(postBusinessState, pageable,"Hoạt động");

        return postMapper.postListToPostSummaryDTOs(queryResult);
    }

    @Override
    public List<PostSummaryDTO> getPostNewest() {
        Sort sort = Sort.by("publishDtm").descending();
        Pageable pageable = PageRequest.of(0, PAGE_SIZE_NEWEST, sort);

        BooleanExpression postBusinessState = PostPredicateGenerator.getBooleanExpressionOnBusinessState(PostBusinessState.PUBLIC);

        Page<Post> queryResult = postRepository.findAll(postBusinessState, pageable);

        return postMapper.postPageToPostSummaryDTOList(queryResult);
    }

    @Override
    public PostSummaryListDTO getPostBySearchTerm(String sortByPublishDtm, Integer page, String searchTerm, Long postCategoryID, Long tagID, Authentication authentication) {
        String tagContent = null;

        if (tagID != null) {
            Optional<Tag> object = tagRepository.findById(tagID);
            if (object.isPresent()) {
                tagContent = object.get().getContent();
            }
        }

        PostSummaryListDTO postSummaryListDTO = postRepository.searchBySearchTerm(sortByPublishDtm, postCategoryID, page, PAGE_SIZE , searchTerm, tagContent, authentication);
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
    public PostDetailsWithStateListDTO getPostDetailsWithState(Predicate predicate, Pageable pageable, PostStateType postStateType, Authentication authentication) {
        //Reset PAGE_SIZE to predefined value.
        pageable = PageRequest.of(pageable.getPageNumber(), PAGE_SIZE, pageable.getSort());

        BooleanExpression authorizationFilter = PostPredicateGenerator.getBooleanExpressionOnAuthentication(authentication);
        BooleanExpression postBusinessState = PostPredicateGenerator.getBooleanExpressionNotDeleted();

        PostDetailsWithStateListDTO queryResult = postRepository.getPostDetailsListWithStateFilter(
                authorizationFilter.and(postBusinessState).and(predicate),
                pageable,
                postStateType
        );

        return queryResult;
    }

    @Override
    public PostSummaryWithStateAndFeedbackListDTO getPostWithStateAndFeedbackUserOwn(Predicate predicate, Pageable pageable, Authentication authentication) {
        BooleanExpression userOwn = PostPredicateGenerator.getBooleanExpressionUserOwn(authentication);
        BooleanExpression notDeleted = PostPredicateGenerator.getBooleanExpressionNotDeleted();

        //Reset PAGE_SIZE to predefined value.
        pageable = PageRequest.of(pageable.getPageNumber(), PAGE_SIZE, pageable.getSort());

        PostSummaryWithStateAndFeedbackListDTO queryResult = postRepository.getPostSummaryStateFeedback(userOwn.and(notDeleted).and(predicate), pageable);

        return queryResult;
    }

    @Override
    public List<PostSuggestionDTO> getRelatedPostSameAuthor(UUID authorID, Long postID, Integer page, Authentication authentication) throws IDNotFoundException, IOException {

        if (page == null) {
            page = 0;
        }

        Optional<Post> optEntity = postRepository.findById(postID);
        if (optEntity.isEmpty()) {
            throw new IDNotFoundException();
        }

        return postRepository.searchRelatedPost(authorID, null ,optEntity.get(), page ,PostBusinessConstant.RELATED_POST_MAX,
                PostBusinessState.PUBLIC, authentication);
    }

    @Override
    public List<PostSuggestionDTO> getRelatedPostSameCategory(Long categoryID, Long postID, Integer page, Authentication authentication) throws IDNotFoundException, IOException {
        if (page == null) {
            page = 0;
        }

        Optional<Post> optEntity = postRepository.findById(postID);
        if (optEntity.isEmpty()) {
            throw new IDNotFoundException();
        }

        return postRepository.searchRelatedPost(null, categoryID ,optEntity.get(), page ,PostBusinessConstant.RELATED_POST_MAX,
                PostBusinessState.PUBLIC, authentication);
    }

    @Override
    public List<PostSuggestionDTO> getRelatedPostByExercise(Long exerciseID, Integer page) throws IDNotFoundException, IOException {
        if (page == null) {
            page = 0;
        }

        Optional<Exercise> object = exerciseRepository.findById(exerciseID);
        if (object.isEmpty()) {
            throw new IDNotFoundException();
        }

        Exercise exercise = object.get();
        final String title = exercise.getTitle();
        final String description = exercise.getDescription();

        return postRepository.searchRelatedPost(null, null, null, title, description, title.concat(description),
                page, PostBusinessConstant.RELATED_POST_MAX, PostBusinessState.PUBLIC, null);
    }

    @Override
    public PostSummaryListDTO getPostSavedByUserOwn(Authentication authentication, Pageable pageable) {
        UUID userID = SecurityUtils.getUserID(authentication);
        if (userID == null) {
            throw new IllegalArgumentException("Cannot extract userID from authentication.");
        }
        BooleanExpression authorizationFilter = PostPredicateGenerator.getBooleanExpressionOnAuthentication(authentication);
        BooleanExpression postBusinessStateFilter = PostPredicateGenerator.getBooleanExpressionOnBusinessState(PostBusinessState.PUBLIC);

        //Reset PAGE_SIZE to predefined value.
        pageable = PageRequest.of(pageable.getPageNumber(), PAGE_SIZE, pageable.getSort());

        PostSummaryListDTO result = userPostSaveRepository.findByUserPostSaveIdUserId(userID, postBusinessStateFilter ,authorizationFilter, pageable);
        return result;
    }

    @Override
    public PostSummaryWithStateListDTO getManagementPost(String searchTerm, PostStateType postStateType, Integer page, String sortByPublishDtm, Long postCategoryID, Authentication authentication) {
        PostSummaryWithStateListDTO dto = postRepository.getManagementPost(sortByPublishDtm,
                postCategoryID,
                page,
                PAGE_SIZE,
                searchTerm,
                postStateType,
                authentication);
        return dto;
    }

    @Override
    public List<PostAvailableActionDTO> getAvailablePostAction(List<Long> postIDs, Authentication authentication) {
        List<PostAvailableActionDTO> postAvailableActionDTOList = new ArrayList<>();

        for (Long postID : postIDs) {
            if (postID == null) {
                continue;
            }

            PostAvailableActionDTO postAvailableActionDTO = new PostAvailableActionDTO();
            postAvailableActionDTO.setId(postID);
            List<String> availableAction = new ArrayList<>();

            if (postPermissionEvaluator.hasPermission(authentication, postID, PostActionPermissionRequest.READ_PERMISSION)) {
                availableAction.add(PostActionAvailableConstant.READ_ACTION);
            }
            if (postPermissionEvaluator.hasPermission(authentication, postID, PostActionPermissionRequest.UPDATE_PERMISSION)) {
                availableAction.add(PostActionAvailableConstant.UPDATE_ACTION);
            }
            if (postPermissionEvaluator.hasPermission(authentication, postID, PostActionPermissionRequest.DELETE_PERMISSION)) {
                availableAction.add(PostActionAvailableConstant.DELETE_ACTION);
            }
            if (postPermissionEvaluator.hasPermission(authentication, postID, PostActionPermissionRequest.SAVE_PERMISSION)) {
                availableAction.add(PostActionAvailableConstant.SAVE_ACTION);
            }
            if (postPermissionEvaluator.hasPermission(authentication, postID, PostActionPermissionRequest.LIKE_PERMISSION)) {
                availableAction.add(PostActionAvailableConstant.LIKE_ACTION);
            }
            if (postPermissionEvaluator.hasPermission(authentication, postID, PostActionPermissionRequest.APPROVE_PERMISSION)) {
                availableAction.add(PostActionAvailableConstant.APPROVE_ACTION);
            }
            if (postPermissionEvaluator.hasPermission(authentication, postID, PostActionPermissionRequest.REPORT_PERMISSION)) {
                availableAction.add(PostActionAvailableConstant.REPORT_ACTION);
            }
            if (postPermissionEvaluator.hasPermission(authentication, postID, PostActionPermissionRequest.COMMENT_PERMISSION)) {
                availableAction.add(PostActionAvailableConstant.COMMENT_ACTION);
            }
            if (postPermissionEvaluator.hasPermission(authentication, postID, HighlightPostPermissionRequest.HIGHLIGHT_POST_CREATE)) {
                availableAction.add(PostActionAvailableConstant.HIGHLIGHT_CREATE_ACTION);
            }
            if (postPermissionEvaluator.hasPermission(authentication, postID, HighlightPostPermissionRequest.HIGHLIGHT_POST_DELETE)) {
                availableAction.add(PostActionAvailableConstant.HIGHLIGHT_DELETE_ACTION);
            }
            if (postPermissionEvaluator.hasPermission(authentication, postID, HighlightPostPermissionRequest.HIGHLIGHT_POST_STICKTOTOP)) {
                availableAction.add(PostActionAvailableConstant.HIGHLIGHT_STICKTOTOP_ACTION);
            }

            postAvailableActionDTO.setAvailableActions(availableAction);

            postAvailableActionDTOList.add(postAvailableActionDTO);
        }
        return postAvailableActionDTOList;
    }

    @Override
    public List<PostSummaryDTO> getTrendingPost() {
        //TODO: Implement real trending function.
        Pageable pageable = PageRequest.of(0, PAGE_SIZE_TRENDING_HOME);
        Predicate publicPost = PostPredicateGenerator.getBooleanExpressionOnBusinessState(PostBusinessState.PUBLIC);
        Page<Post> trendingPost = postRepository.findAll(publicPost, pageable);
        return postMapper.postListToPostSummaryDTOs(trendingPost.getContent());
    }

    @Override
    public void calculateAllPageViewAvg() {

    }

}