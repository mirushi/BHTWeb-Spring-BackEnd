package com.bhtcnpm.website.controller.Post;

import com.bhtcnpm.website.model.dto.PostCommentReport.PostCommentReportListDTO;
import com.bhtcnpm.website.model.dto.PostCommentReport.PostCommentReportRequestDTO;
import com.bhtcnpm.website.model.dto.PostCommentReport.PostCommentReportResolveRequestDTO;
import com.bhtcnpm.website.model.exception.IDNotFoundException;
import com.bhtcnpm.website.service.Post.PostCommentReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;

@RestController
@Validated
@RequiredArgsConstructor
public class UserPostCommentReportController {

    private final PostCommentReportService postCommentReportService;

    @PostMapping("/posts/comments/{id}/report")
    @ResponseBody
    public ResponseEntity reportPost (@PathVariable("id") Long commentID,
                                      @RequestBody PostCommentReportRequestDTO dto,
                                      Authentication authentication) {
        postCommentReportService.createNewReport(commentID, dto, authentication);

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/posts/comments/resolveReport/{id}")
    @ResponseBody
    public ResponseEntity resolveReport (@PathVariable("id") Long reportID,
                                         @RequestBody PostCommentReportResolveRequestDTO dto,
                                         Authentication authentication) throws IDNotFoundException {
        postCommentReportService.resolveReport(reportID, dto, authentication);

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/posts/comments/report")
    @ResponseBody
    public ResponseEntity<PostCommentReportListDTO> getUserReports (@PageableDefault @Nullable Pageable pageable,
                                                                    @Nullable Boolean isResolvedReport) {
        PostCommentReportListDTO list = postCommentReportService.getUserReports(pageable, isResolvedReport);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

}
