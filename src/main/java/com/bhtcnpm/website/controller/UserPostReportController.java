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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

@RestController
@Validated
@RequiredArgsConstructor
public class UserPostReportController {

    private final UserPostReportService userPostReportService;

    @PostMapping("/post/{id}/report")
    @ResponseBody
    public ResponseEntity reportPost (@RequestParam Long postID, @RequestBody UserPostReportRequestDTO dto) throws IDNotFoundException {
        //TODO: We'll use a hard-coded userID for now. We'll get userID from user login token later.
        Long userID = 1L;

        userPostReportService.createNewReport(userID, postID, dto);

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/post/resolveReport/{id}")
    @ResponseBody
    public ResponseEntity resolveReport (@RequestParam Long reportID, @RequestBody UserPostReportResolveRequestDTO dto) throws IDNotFoundException {
        //TODO: We'll use a hard-coded userID for now. We'll get userID from user login token later.
        Long userID = 1L;

        userPostReportService.resolveReport(userID, reportID, dto);

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/post/report")
    @ResponseBody
    public ResponseEntity<UserPostReportListDTO> getUserReports (@RequestParam(value = "page", required = false) Integer page,
                                                                 @RequestParam(value = "sort", required = false) String sort,
                                                                 @PageableDefault @Nullable Pageable pageable,
                                                                 @Nullable Boolean isResolvedReport) {
        UserPostReportListDTO list = userPostReportService.getUserReports(pageable, isResolvedReport);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
