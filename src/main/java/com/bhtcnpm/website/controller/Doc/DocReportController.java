package com.bhtcnpm.website.controller.Doc;

import com.bhtcnpm.website.model.dto.DocReport.DocReportListDTO;
import com.bhtcnpm.website.model.dto.DocReport.DocReportRequestDTO;
import com.bhtcnpm.website.model.dto.DocReport.DocReportResolveRequestDTO;
import com.bhtcnpm.website.model.exception.IDNotFoundException;
import com.bhtcnpm.website.model.validator.dto.Doc.DocID;
import com.bhtcnpm.website.service.Doc.DocReportService;
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
public class DocReportController {
    private final DocReportService docReportService;

    @PostMapping("/documents/{id}/report")
    @ResponseBody
    public ResponseEntity reportDoc (@PathVariable("id") @DocID Long docID,
                                     @RequestBody DocReportRequestDTO dto,
                                     Authentication authentication) throws IDNotFoundException {
        docReportService.createNewReport(docID, dto, authentication);

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/documents/resolveReport/{id}")
    @ResponseBody
    public ResponseEntity resolveReport (@PathVariable("id") Long reportID,
                                         @RequestBody DocReportResolveRequestDTO dto,
                                         Authentication authentication) {
        docReportService.resolveReport(reportID, dto, authentication);

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/documents/report")
    @ResponseBody
    public ResponseEntity<DocReportListDTO> getUserReports (@PageableDefault @Nullable Pageable pageable,
                                                            @Nullable Boolean isResolvedReport) {
        DocReportListDTO list = docReportService.getUserReports(pageable, isResolvedReport);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
