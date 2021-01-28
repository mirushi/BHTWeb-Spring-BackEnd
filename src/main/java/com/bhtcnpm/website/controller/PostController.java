package com.bhtcnpm.website.controller;

import com.bhtcnpm.website.model.dto.Post.*;
import com.bhtcnpm.website.model.entity.PostEntities.Post;
import com.bhtcnpm.website.service.PostService;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.websocket.server.PathParam;
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
}
