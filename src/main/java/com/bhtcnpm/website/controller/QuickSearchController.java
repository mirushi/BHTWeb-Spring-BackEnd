package com.bhtcnpm.website.controller;

import com.bhtcnpm.website.model.dto.QuickSearch.QuickSearchResultDTO;
import com.bhtcnpm.website.service.QuickSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quicksearch")
@Validated
@RequiredArgsConstructor
public class QuickSearchController {

    private final QuickSearchService quickSearchService;

    @GetMapping("queries")
    @ResponseBody
    public ResponseEntity<QuickSearchResultDTO> quickSearch (@RequestParam String searchTerm) {
        QuickSearchResultDTO quickSearchResultDTO = quickSearchService.quickSearch(searchTerm);

        return new ResponseEntity<>(quickSearchResultDTO, HttpStatus.OK);
    }
}
