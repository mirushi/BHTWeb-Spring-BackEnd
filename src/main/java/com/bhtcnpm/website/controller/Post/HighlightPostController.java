package com.bhtcnpm.website.controller.Post;

import com.bhtcnpm.website.model.dto.Post.HighlightPostDTO;
import com.bhtcnpm.website.model.dto.Post.HighlightPostRequestDTO;
import com.bhtcnpm.website.model.validator.dto.Post.PostID;
import com.bhtcnpm.website.service.Post.HighlightPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
    public ResponseEntity createHighlightPost (@RequestBody @Valid HighlightPostRequestDTO highlightPostRequestDTO,
                                               Authentication authentication) {
        highlightPostService.createHighlightPost(highlightPostRequestDTO.getId(), authentication);

        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping
    @ResponseBody
    public ResponseEntity deleteHighlightPost (@RequestParam("id") @PostID Long postID) {
        highlightPostService.deleteHighlightPost(postID);

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("stickToTop")
    @ResponseBody
    public ResponseEntity stickHighlightPost (@RequestParam("id") @PostID Long postID) {
        highlightPostService.stickToTop(postID);

        return new ResponseEntity(HttpStatus.OK);
    }
}
