package com.bhtcnpm.website.controller;

import com.bhtcnpm.website.model.dto.Post.PostStatisticDTO;
import com.bhtcnpm.website.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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



}
