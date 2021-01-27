package com.bhtcnpm.website.controller;

import com.bhtcnpm.website.model.dto.Post.PostDetailsDTO;
import com.bhtcnpm.website.model.dto.Post.PostStatisticDTO;
import com.bhtcnpm.website.model.dto.Post.PostSummaryDTO;
import com.bhtcnpm.website.model.dto.Post.PostSummaryListDTO;
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

    @GetMapping(value = "/{id}")
    @ResponseBody
    public ResponseEntity<PostDetailsDTO> getPostDetails (@PathVariable Long id) {
        PostDetailsDTO postDetailsDTO = postService.getPostDetails(id);

        return new ResponseEntity<>(postDetailsDTO, HttpStatus.OK);
    }

}
