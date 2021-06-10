package com.bhtcnpm.website.controller;

import com.bhtcnpm.website.model.dto.Post.HighlightPostDTO;
import com.bhtcnpm.website.model.dto.Post.HighlightPostRequestDTO;
import com.bhtcnpm.website.model.dto.Post.HighlightPostUpdateListDTO;
import com.bhtcnpm.website.service.HighlightPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/posts/highlightPosts")
@Validated
@RequiredArgsConstructor
public class HighlightPostController {

    private final HighlightPostService highlightPostService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<HighlightPostDTO>> getHighlightPosts () {
        List<HighlightPostDTO> highlightPostDTOS = highlightPostService.getAllHighlightedPost();
        return new ResponseEntity<>(highlightPostDTOS, HttpStatus.OK);
    }

    @GetMapping("ids")
    @ResponseBody
    public ResponseEntity<List<Long>> getHighlightPostIDs () {
        List<Long> highlightPostIds = highlightPostService.getAllHighlightedPostIDs();
        return new ResponseEntity<>(highlightPostIds, HttpStatus.OK);
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity createHighlightPost (@RequestBody HighlightPostRequestDTO highlightPostRequestDTO,
                                               Authentication authentication) {
        highlightPostService.createHighlightPost(highlightPostRequestDTO.getId(), authentication);

        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping
    @ResponseBody
    public ResponseEntity deleteHighlightPost (@RequestParam("id") Long postID) {
        highlightPostService.deleteHighlightPost(postID);

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("stickToTop")
    @ResponseBody
    public ResponseEntity stickHighlightPost (@RequestParam("id") Long postID) {
        highlightPostService.stickToTop(postID);

        return new ResponseEntity(HttpStatus.OK);
    }
}
