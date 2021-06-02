package com.bhtcnpm.website.controller;

import com.bhtcnpm.website.model.dto.ReportReason.ReportReasonDTO;
import com.bhtcnpm.website.service.ReportReasonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reportReason")
@Validated
@RequiredArgsConstructor
public class ReportReasonController {

    private final ReportReasonService reportReasonService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<ReportReasonDTO>> getAllReportReason () {
        return new ResponseEntity<>(reportReasonService.getAllReportReason(), HttpStatus.OK);
    }
}
