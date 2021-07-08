package com.bhtcnpm.website.controller.Doc;

import com.bhtcnpm.website.model.dto.DocCommentReport.DocCommentReportListDTO;
import com.bhtcnpm.website.model.dto.DocCommentReport.DocCommentReportRequestDTO;
import com.bhtcnpm.website.model.dto.DocCommentReport.DocCommentReportResolveRequestDTO;
import com.bhtcnpm.website.model.exception.IDNotFoundException;
import com.bhtcnpm.website.service.Doc.DocCommentReportService;
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
public class DocCommentReportController {
    private final DocCommentReportService docCommentReportService;

    @PostMapping("/documents/comments/{id}/report")
    @ResponseBody
    public ResponseEntity reportDocComment (@PathVariable("id") Long commentID,
                                            @RequestBody DocCommentReportRequestDTO dto,
                                            Authentication authentication) {
        docCommentReportService.createNewReport(commentID, dto, authentication);

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/documents/comments/resolveReport/{id}")
    @ResponseBody
    public ResponseEntity resolveReport (@PathVariable("id") Long reportID,
                                         @RequestBody DocCommentReportResolveRequestDTO dto,
                                         Authentication authentication) throws IDNotFoundException {
        docCommentReportService.resolveReport(reportID, dto, authentication);

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/documents/comments/report")
    @ResponseBody
    public ResponseEntity<DocCommentReportListDTO> getUserReports (@PageableDefault @Nullable Pageable pageable,
                                                                   @Nullable Boolean isResolvedReport) {
        DocCommentReportListDTO list = docCommentReportService.getUserReports(pageable, isResolvedReport);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
