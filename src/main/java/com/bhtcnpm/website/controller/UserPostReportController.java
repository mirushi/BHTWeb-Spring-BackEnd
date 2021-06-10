package com.bhtcnpm.website.controller;

import com.bhtcnpm.website.model.dto.UserPostReport.UserPostReportListDTO;
import com.bhtcnpm.website.model.dto.UserPostReport.UserPostReportRequestDTO;
import com.bhtcnpm.website.model.dto.UserPostReport.UserPostReportResolveRequestDTO;
import com.bhtcnpm.website.model.exception.IDNotFoundException;
import com.bhtcnpm.website.service.UserPostReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@RestController
@Validated
@RequiredArgsConstructor
public class UserPostReportController {

    private final UserPostReportService userPostReportService;

    @PostMapping("/posts/{id}/report")
    @ResponseBody
    public ResponseEntity reportPost (@PathVariable("id") Long postID,
                                      @RequestBody UserPostReportRequestDTO dto,
                                      Authentication authentication) throws IDNotFoundException {
        userPostReportService.createNewReport(postID, dto, authentication);

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/posts/resolveReport/{id}")
    @ResponseBody
    public ResponseEntity resolveReport (@PathVariable("id") Long reportID,
                                         @RequestBody UserPostReportResolveRequestDTO dto,
                                         Authentication authentication) throws IDNotFoundException {
        userPostReportService.resolveReport(reportID, dto, authentication);

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/posts/report")
    @ResponseBody
    public ResponseEntity<UserPostReportListDTO> getUserReports (@RequestParam(value = "page", required = false) Integer page,
                                                                 @RequestParam(value = "sort", required = false) String sort,
                                                                 @PageableDefault @Nullable Pageable pageable,
                                                                 @Nullable Boolean isResolvedReport) {
        UserPostReportListDTO list = userPostReportService.getUserReports(pageable, isResolvedReport);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
