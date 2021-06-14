package com.bhtcnpm.website.controller;

import com.bhtcnpm.website.constant.ApiSortOrder;
import com.bhtcnpm.website.model.binding.IgnorePostStateTypeBinding;
import com.bhtcnpm.website.model.dto.Post.*;
import com.bhtcnpm.website.model.entity.PostEntities.Post;
import com.bhtcnpm.website.model.entity.enumeration.PostState.PostStateType;
import com.bhtcnpm.website.model.exception.IDNotFoundException;
import com.bhtcnpm.website.model.validator.dto.Pagination;
import com.bhtcnpm.website.model.validator.dto.Post.PostActionRequestSize;
import com.bhtcnpm.website.model.validator.dto.Post.PostFeedback;
import com.bhtcnpm.website.model.validator.dto.Post.PostID;
import com.bhtcnpm.website.model.validator.dto.Post.PostStatisticRequestSize;
import com.bhtcnpm.website.service.PostService;
import com.bhtcnpm.website.service.PostViewService;
import com.bhtcnpm.website.util.HttpIPUtils;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/posts")
@Validated
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    private final PostViewService postViewService;

    @GetMapping(value = "/statistics")
    @ResponseBody
    public ResponseEntity<List<PostStatisticDTO>> getPostStatistics (
            @RequestParam @PostStatisticRequestSize List<@PostID Long> postIDs,
            Authentication authentication
    ) {
        List<PostStatisticDTO> postStatisticDTOS = postService.getPostStatistic(postIDs, authentication);

        return new ResponseEntity<>(postStatisticDTOS, HttpStatus.OK);
    }

    @GetMapping(value = "/actionAvailable")
    @ResponseBody
    public ResponseEntity<List<PostAvailableActionDTO>> getPostActionAvailable (
            @RequestParam @PostActionRequestSize List<@PostID Long> postIDs,
            Authentication authentication
    ) {
        List<PostAvailableActionDTO> availableActionDTOList = postService.getAvailablePostAction(postIDs, authentication);

        return new ResponseEntity<>(availableActionDTOList, HttpStatus.OK);
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<PostSummaryListDTO> getPostSummary (@QuerydslPredicate(root = Post.class) Predicate predicate,
                                                              @RequestParam(value = "mostLiked", required = false) boolean mostLiked,
                                                              @RequestParam(value = "mostViewed", required = false) boolean mostViewed,
                                                              @PageableDefault @Nullable Pageable pageable, Authentication authentication) {
        PostSummaryListDTO postSummaryListDTO = postService.getPostSummary(predicate, pageable, mostLiked, mostViewed, authentication);

        return new ResponseEntity<>(postSummaryListDTO, HttpStatus.OK);
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<PostDetailsDTO> postPostDetails (@RequestBody @Valid PostRequestDTO postRequestDTO,
                                                           Authentication authentication) {
        PostDetailsDTO detailsDTO = postService.createPost(postRequestDTO, authentication);

        return new ResponseEntity<>(detailsDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    @ResponseBody
    public ResponseEntity<PostDetailsDTO> getPostDetails (@PathVariable @PostID Long id,
                                                          HttpServletRequest servletRequest,
                                                          Authentication authentication) {
        PostDetailsDTO postDetailsDTO = postService.getPostDetails(id);

        if (postDetailsDTO == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        String ipAddress = HttpIPUtils.getClientIPAddress(servletRequest);

        postViewService.addPostView(id, authentication, ipAddress);

        return new ResponseEntity<>(postDetailsDTO, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    @ResponseBody
    public ResponseEntity<PostDetailsDTO> putPostDetails (@RequestBody @Valid PostRequestDTO postRequestDTO,
                                                          @PathVariable @PostID Long id,
                                                          Authentication authentication) {
        PostDetailsDTO postDetailsDTO = postService.editPost(postRequestDTO, id, authentication);

        return new ResponseEntity<>(postDetailsDTO, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public ResponseEntity deletePost (@PathVariable @PostID Long id,
                                      Authentication authentication) {
        Boolean result = postService.deletePost(id, authentication);

        if (result) {
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/{id}/approval")
    @ResponseBody
    public ResponseEntity postPostApproval (@PathVariable @PostID Long id,
                                            Authentication authentication) {
        Boolean result = postService.approvePost(id, authentication);

        if (result) {
            return new ResponseEntity(HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(value = "/{id}/approval")
    @ResponseBody
    public ResponseEntity postDeleteApproval (@PathVariable @PostID Long id) {
        Boolean result = postService.deletePostApproval(id);

        if (result) {
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/{id}/likeStatus")
    @ResponseBody
    public ResponseEntity postLikeStatus (@PathVariable @PostID Long id,
                                          Authentication authentication) {
        Boolean result = postService.createUserPostLike(id, authentication);

        if (result) {
            return new ResponseEntity(HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(value = "/{id}/likeStatus")
    @ResponseBody
    public ResponseEntity deleteLikeStatus (@PathVariable @PostID Long id,
                                            Authentication authentication) {
        Boolean result = postService.deleteUserPostLike(id, authentication);

        if (result) {
            return new ResponseEntity(HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/{id}/rejection")
    @ResponseBody
    public ResponseEntity postRejection (@PathVariable @PostID Long id,
                                         Authentication authentication) {

        Boolean result = postService.rejectPost(id, authentication);

        if (result) {
            return new ResponseEntity(HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/{id}/rejectionWithFeedback")
    @ResponseBody
    public ResponseEntity postRejectionWithFeedback (@PathVariable @PostID Long id,
                                                     @RequestBody @PostFeedback String feedback,
                                                     Authentication authentication) {
        Boolean result = postService.rejectPostWithFeedback(id, feedback);

        if (result) {
            return new ResponseEntity(HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/{id}/savedStatus")
    @ResponseBody
    public ResponseEntity postSavedStatus (@PathVariable @PostID Long id,
                                           Authentication authentication) {
        Boolean result = postService.createSavedStatus(id, authentication);

        if (result) {
            return new ResponseEntity(HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(value = "/{id}/savedStatus")
    @ResponseBody
    public ResponseEntity deleteSavedStatus (@PathVariable @PostID Long id,
                                             Authentication authentication) {
        Boolean result = postService.deleteSavedStatus(id, authentication);

        if (result) {
            return new ResponseEntity(HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/savedPost")
    @ResponseBody
    public ResponseEntity<PostSummaryListDTO> getPostSavedByUserOwn(
            @QuerydslPredicate(root = Post.class) Predicate predicate,
            @PageableDefault @Nullable Pageable pageable,
            Authentication authentication
    ) {
        PostSummaryListDTO postsSavedByUser = postService.getPostSavedByUserOwn(authentication, pageable);
        return new ResponseEntity(postsSavedByUser, HttpStatus.OK);
    }

    @GetMapping("newactivities")
    @ResponseBody
    public ResponseEntity<List<PostSummaryDTO>> getNewActivities () {
        return new ResponseEntity<>(postService.getPostWithActivityCategory(), HttpStatus.OK);
    }

    @GetMapping("newest")
    @ResponseBody
    public ResponseEntity<List<PostSummaryDTO>> getNewestPost () {
        return new ResponseEntity<>(postService.getPostNewest(), HttpStatus.OK);
    }

    @GetMapping("searchFilter")
    @ResponseBody
    public ResponseEntity<PostSummaryListDTO> searchFilter (
            @RequestParam String searchTerm,
            @RequestParam(value = "page") @Pagination Integer page,
            @RequestParam(value = "sortByPublishDtm", required = false) String sortByPublishDtm,
            @RequestParam(value = "postCategoryID", required = false) Long postCategoryID,
            @RequestParam(value = "tags", required = false) Long tagID,
            Authentication authentication) {
        return new ResponseEntity<>(postService.getPostBySearchTerm(sortByPublishDtm, page, searchTerm, postCategoryID, tagID, authentication), HttpStatus.OK);
    }

    @GetMapping("relatedSameAuthor")
    @ResponseBody
    public ResponseEntity<List<PostSuggestionDTO>> relatedSameAuthor (
            @RequestParam("authorID") UUID authorID,
            @RequestParam("postID") @PostID Long postID,
            @RequestParam(value = "page", required = false) @Pagination Integer page,
            Authentication authentication) throws IDNotFoundException, IOException {
        return new ResponseEntity<>(postService.getRelatedPostSameAuthor(authorID, postID, page, authentication), HttpStatus.OK);
    }

    @GetMapping("relatedSameCategory")
    @ResponseBody
    public ResponseEntity<List<PostSuggestionDTO>> relatedSameCategory (
            @RequestParam("categoryID") Long categoryID,
            @RequestParam("postID") @PostID Long postID,
            @RequestParam(value = "page", required = false) @Pagination Integer page, Authentication authentication) throws IDNotFoundException, IOException {
        return new ResponseEntity<>(postService.getRelatedPostSameCategory(categoryID, postID, page, authentication), HttpStatus.OK);
    }

    @GetMapping("myPosts")
    @ResponseBody
    public ResponseEntity<PostSummaryWithStateAndFeedbackListDTO> getMyPosts (
            @QuerydslPredicate(root = Post.class) Predicate predicate,
            @PageableDefault @Nullable Pageable pageable,
            Authentication authentication) {
        return new ResponseEntity<>(postService.getPostWithStateAndFeedbackUserOwn(predicate, pageable, authentication), HttpStatus.OK);
    }

    @GetMapping("pendingApproval")
    @ResponseBody
    public ResponseEntity<PostDetailsWithStateListDTO> getPendingApprovalPosts (
            @QuerydslPredicate(root = Post.class, bindings = IgnorePostStateTypeBinding.class) Predicate predicate,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "sort", required = false) String sort,
            @PageableDefault @Nullable Pageable pageable,
            Authentication authentication
    ) {
        return new ResponseEntity<>(postService.getPostDetailsWithState(predicate, pageable, PostStateType.PENDING_APPROVAL, authentication), HttpStatus.OK);
    }

    @GetMapping("getManagementPost")
    @ResponseBody
    public ResponseEntity<PostSummaryWithStateListDTO> getManagementPost (
            @RequestParam(value = "searchTerm", required = false) String searchTerm,
            @RequestParam(value = "postState", required = false) PostStateType postStateType,
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "sortByPublishDtm", required = false) String sortByPublishDtm,
            @RequestParam(value = "postCategoryID", required = false) Long postCategoryID,
            Authentication authentication
    ) {
        return new ResponseEntity<>(postService.getManagementPost(searchTerm,
                postStateType,
                page,
                sortByPublishDtm,
                postCategoryID,
                authentication), HttpStatus.OK);
    }

}
