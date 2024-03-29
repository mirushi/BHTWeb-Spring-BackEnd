package com.bhtcnpm.website.controller.Post;

import com.bhtcnpm.website.model.dto.UserPostReport.UserPostReportListDTO;
import com.bhtcnpm.website.model.dto.UserPostReport.UserPostReportRequestDTO;
import com.bhtcnpm.website.model.dto.UserPostReport.UserPostReportResolveRequestDTO;
import com.bhtcnpm.website.model.exception.IDNotFoundException;
import com.bhtcnpm.website.model.validator.dto.Post.PostID;
import com.bhtcnpm.website.model.validator.dto.PostReport.PostReportID;
import com.bhtcnpm.website.service.Post.UserPostReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import javax.validation.Valid;

@RestController
@Validated
@RequiredArgsConstructor
public class UserPostReportController {

    private final UserPostReportService userPostReportService;

    @PostMapping("/posts/{id}/report")
    @ResponseBody
    public ResponseEntity reportPost (@PathVariable("id") @PostID Long postID,
                                      @RequestBody @Valid UserPostReportRequestDTO dto,
                                      Authentication authentication) throws IDNotFoundException {
        userPostReportService.createNewReport(postID, dto, authentication);

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/posts/resolveReport/{id}")
    @ResponseBody
    public ResponseEntity resolveReport (@PathVariable("id") @PostReportID Long reportID,
                                         @RequestBody @Valid UserPostReportResolveRequestDTO dto,
                                         Authentication authentication) throws IDNotFoundException {
        userPostReportService.resolveReport(reportID, dto, authentication);

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/posts/report")
    @ResponseBody
    public ResponseEntity<UserPostReportListDTO> getUserReports (@PageableDefault @Nullable Pageable pageable,
                                                                 @Nullable Boolean isResolvedReport) {
        UserPostReportListDTO list = userPostReportService.getUserReports(pageable, isResolvedReport);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
