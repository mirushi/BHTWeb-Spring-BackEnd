package com.bhtcnpm.website.controller;

import com.bhtcnpm.website.model.binding.IgnorePostStateTypeBinding;
import com.bhtcnpm.website.model.dto.Post.*;
import com.bhtcnpm.website.model.entity.PostEntities.Post;
import com.bhtcnpm.website.model.entity.enumeration.PostState.PostStateType;
import com.bhtcnpm.website.model.exception.IDNotFoundException;
import com.bhtcnpm.website.service.PostService;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import javax.annotation.security.RolesAllowed;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/posts")
@Validated
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping(value = "/statistic")
    @ResponseBody
    public ResponseEntity<List<PostStatisticDTO>> getPostStatistics (@RequestParam List<Long> postIDs) {
        //TODO: We'll use a hard-coded userID for now. We'll get userID from user login token later.
        Long userID = 1L;

        List<PostStatisticDTO> postStatisticDTOS = postService.getPostStatistic(postIDs, userID);

        return new ResponseEntity<>(postStatisticDTOS, HttpStatus.OK);
    }

    @GetMapping
    @ResponseBody
    @RolesAllowed("ROLE_POST_READ")
    public ResponseEntity<PostSummaryListDTO> getPostSummary (@QuerydslPredicate(root = Post.class)Predicate predicate, @NotNull @Min(0) Integer paginator) {
        PostSummaryListDTO postSummaryListDTO = postService.getPostSummary(predicate, paginator);

        return new ResponseEntity<>(postSummaryListDTO, HttpStatus.OK);
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<PostDetailsDTO> postPostDetails (@RequestBody PostRequestDTO postRequestDTO) {
        //TODO: We'll use a hard-coded userID for now. We'll get userID from user login token later.
        Long userID = 1L;

        PostDetailsDTO detailsDTO = postService.createPost(postRequestDTO, userID);

        return new ResponseEntity<>(detailsDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    @ResponseBody
    public ResponseEntity<PostDetailsDTO> getPostDetails (@PathVariable Long id) {
        PostDetailsDTO postDetailsDTO = postService.getPostDetails(id);

        return new ResponseEntity<>(postDetailsDTO, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    @ResponseBody
    public ResponseEntity<PostDetailsDTO> putPostDetails (@RequestBody PostRequestDTO postRequestDTO, @PathVariable Long id) {
        //TODO: We'll use a hard-coded userID for now. We'll get userID from user login token later.
        Long userID = 1L;

        PostDetailsDTO postDetailsDTO = postService.editPost(postRequestDTO, id, userID);

        return new ResponseEntity<>(postDetailsDTO, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public ResponseEntity deletePost (@PathVariable Long id) {
        //TODO: We'll use a hard-coded userID for now. We'll get userID from user login token later.
        Long userID = 1L;

        Boolean result = postService.deletePost(userID, id);

        if (result) {
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/{id}/approval")
    @ResponseBody
    public ResponseEntity postPostApproval (@PathVariable Long id) {
        //TODO: We'll use a hard-coded userID for now. We'll get userID from user login token later.
        Long userID = 1L;

        Boolean result = postService.approvePost(id, userID);

        if (result) {
            return new ResponseEntity(HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(value = "/{id}/approval")
    @ResponseBody
    public ResponseEntity postDeleteApproval (@PathVariable Long id) {
        //TODO: We'll use a hard-coded userID for now. We'll get userID from user login token later.
        Long userID = 1L;

        Boolean result = postService.deletePostApproval(id);

        if (result) {
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/{id}/likeStatus")
    @ResponseBody
    public ResponseEntity postLikeStatus (@PathVariable Long id) {
        //TODO: We'll use a hard-coded userID for now. We'll get userID from user login token later.
        Long userID = 1L;

        Boolean result = postService.createUserPostLike(id, userID);

        if (result) {
            return new ResponseEntity(HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(value = "/{id}/likeStatus")
    @ResponseBody
    public ResponseEntity deleteLikeStatus (@PathVariable Long id) {
        //TODO: We'll use a hard-coded userID for now. We'll get userID from user login token later.
        Long userID = 1L;

        Boolean result = postService.deleteUserPostLike(id, userID);

        if (result) {
            return new ResponseEntity(HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/{id}/rejection")
    @ResponseBody
    public ResponseEntity postRejection (@PathVariable Long id) {
        //TODO: We'll use a hard-coded userID for now. We'll get userID from user login token later.
        Long userID = 1L;

        Boolean result = postService.rejectPost(id, userID);

        if (result) {
            return new ResponseEntity(HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/{id}/rejectionWithFeedback")
    @ResponseBody
    public ResponseEntity postRejectionWithFeedback (@PathVariable Long id, @RequestBody String feedback) {
        //TODO: We'll use a hard-coded userID for now. We'll get userID from user login token later.
        Long userID = 1L;

        Boolean result = postService.rejectPostWithFeedback(id, feedback);

        if (result) {
            return new ResponseEntity(HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/{id}/savedStatus")
    @ResponseBody
    public ResponseEntity postSavedStatus (@PathVariable Long id) {
        //TODO: We'll use a hard-coded userID for now. We'll get userID from user login token later.
        Long userID = 1L;

        Boolean result = postService.createSavedStatus(id, userID);

        if (result) {
            return new ResponseEntity(HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(value = "/{id}/savedStatus")
    @ResponseBody
    public ResponseEntity deleteSavedStatus (@PathVariable Long id) {
        //TODO: We'll use a hard-coded userID for now. We'll get userID from user login token later.
        Long userID = 1L;

        Boolean result = postService.deleteSavedStatus(id, userID);

        if (result) {
            return new ResponseEntity(HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/savedBy")
    @ResponseBody
    public ResponseEntity<PostSummaryListDTO> getPostSavedByUserId (
            @QuerydslPredicate(root = Post.class) Predicate predicate,
            @RequestParam("userID") Long userID,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "sort", required = false) String sort,
            @PageableDefault @Nullable Pageable pageable
    ) {
        PostSummaryListDTO postsSavedByUser = postService.getPostSavedByUserID(userID, pageable);
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
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "sortByPublishDtm", required = false) String sortByPublishDtm,
            @RequestParam(value = "postCategoryID", required = false) Long postCategoryID) {
        return new ResponseEntity<>(postService.getPostBySearchTerm(sortByPublishDtm, page, searchTerm, postCategoryID), HttpStatus.OK);
    }

    @GetMapping("relatedSameAuthor")
    @ResponseBody
    public ResponseEntity<List<PostSuggestionDTO>> relatedSameAuthor (
            @RequestParam("authorID") Long authorID,
            @RequestParam("postID") Long postID,
            @RequestParam(value = "page", required = false) Integer page) throws IDNotFoundException, IOException {
        return new ResponseEntity<>(postService.getRelatedPostSameAuthor(authorID, postID, page), HttpStatus.OK);
    }

    @GetMapping("relatedSameCategory")
    @ResponseBody
    public ResponseEntity<List<PostSuggestionDTO>> relatedSameCategory (
            @RequestParam("categoryID") Long categoryID,
            @RequestParam("postID") Long postID,
            @RequestParam(value = "page", required = false) Integer page) throws IDNotFoundException, IOException {
        return new ResponseEntity<>(postService.getRelatedPostSameCategory(categoryID, postID, page), HttpStatus.OK);
    }

    @GetMapping("myPosts")
    @ResponseBody
    public ResponseEntity<PostSummaryWithStateAndFeedbackListDTO> getMyPosts (
            @QuerydslPredicate(root = Post.class) Predicate predicate,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "sort", required = false) String sort,
            @PageableDefault @Nullable Pageable pageable) {
        return new ResponseEntity<>(postService.getPostWithStateAndFeedback(predicate, pageable), HttpStatus.OK);
    }

    @GetMapping("pendingApproval")
    @ResponseBody
    public ResponseEntity<PostDetailsWithStateListDTO> getPendingApprovalPosts (
            @QuerydslPredicate(root = Post.class, bindings = IgnorePostStateTypeBinding.class) Predicate predicate,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "sort", required = false) String sort,
            @PageableDefault @Nullable Pageable pageable
    ) {
        return new ResponseEntity<>(postService.getPostDetailsWithState(predicate, pageable, PostStateType.PENDING_APPROVAL), HttpStatus.OK);
    }

    @GetMapping("getManagementPost")
    @ResponseBody
    public ResponseEntity<PostSummaryWithStateListDTO> getManagementPost (
            @RequestParam(value = "searchTerm", required = false) String searchTerm,
            @RequestParam(value = "postState", required = false) PostStateType postStateType,
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "sortByPublishDtm", required = false) String sortByPublishDtm,
            @RequestParam(value = "postCategoryID", required = false) Long postCategoryID
    ) {
        return new ResponseEntity<>(postService.getManagementPost(searchTerm,
                postStateType,
                page,
                sortByPublishDtm,
                postCategoryID), HttpStatus.OK);
    }

}
