package com.bhtcnpm.website.controller;

import com.bhtcnpm.website.model.dto.Doc.*;
import com.bhtcnpm.website.model.entity.DocEntities.Doc;
import com.bhtcnpm.website.service.DocService;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/documents")
@Validated
@RequiredArgsConstructor
public class DocController {

    private final DocService docService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<DocDetailsListDTO> getAllDocuments (@QuerydslPredicate(root = Doc.class) Predicate predicate, @NotNull @Min(0) Integer paginator) {
        DocDetailsListDTO result = docService.getAllDoc(predicate,paginator);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("{id}")
    @ResponseBody
    public ResponseEntity<DocDetailsDTO> putDocument (@PathVariable Long id, @RequestBody DocRequestDTO docRequestDTO) {
        //TODO: We'll use a hard-coded userID for now. We'll get userID from user login token later.
        Long userID = 1L;

        DocDetailsDTO docDetailsDTO = docService.putDoc(id, userID, docRequestDTO);
        return new ResponseEntity<>(docDetailsDTO, HttpStatus.OK);
    }

//    @PostMapping
//    @ResponseBody
//    public ResponseEntity<DocDetailsDTO> postDocument (@RequestBody DocRequestDTO docRequestDTO) {
//        //TODO: We'll use a hard-coded userID for now. We'll get userID from user login token later.
//        Long userID = 1L;
//
//        DocDetailsDTO docDetailsDTO = docService.
//    }

    @GetMapping("trending")
    @ResponseBody
    public ResponseEntity<List<DocSummaryDTO>> getTrendingDocs () {
        return new ResponseEntity<>(docService.getTrending(), HttpStatus.OK);
    }

    @PostMapping("{id}/approval")
    @ResponseBody
    public ResponseEntity postDocApproval (@PathVariable Long id) {
        //TODO: We'll use a hard-coded userID for now. We'll get userID from user login token later.
        Long userID = 1L;

        Boolean result = docService.postApproval(id, userID);
        if (result) {
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("{id}/approval")
    @ResponseBody
    public ResponseEntity deleteDocApproval (@PathVariable Long id) {
        //TODO: We'll use a hard-coded userID for now. We'll get userID from user login token later.
        Long userID = 1L;

        Boolean result = docService.deleteApproval(id);
        if (result) {
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("{id}/downloadCount")
    @ResponseBody
    public ResponseEntity increaseDownloadCount (@PathVariable Long id) {
        //TODO: We'll use a hard-coded userID for now. We'll get userID from user login token later.
        Long userID = 1L;

        Boolean result = docService.increaseDownloadCount(id, userID);

        if (result) {
            return new ResponseEntity(HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("{id}/rejection")
    @ResponseBody
    public ResponseEntity rejectDoc (@PathVariable Long id) {
        //TODO: We'll use a hard-coded userID for now. We'll get userID from user login token later.
        Long userID = 1L;

        Boolean result = docService.postReject(id, userID);

        if (result) {
            return new ResponseEntity(HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("{id}/rejection")
    @ResponseBody
    public ResponseEntity undoRejectDoc (@PathVariable Long id) {
        //TODO: We'll use a hard-coded userID for now. We'll get userID from user login token later.
        Long userID = 1L;

        Boolean result = docService.undoReject(id, userID);

        if (result) {
            return new ResponseEntity(HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("{id}/related")
    @ResponseBody
    public ResponseEntity<List<DocDetailsDTO>> getRelatedDocs (@PathVariable Long id) {
        List<DocDetailsDTO> docDetailsDTOs = docService.getRelatedDocs(id);

        return new ResponseEntity<>(docDetailsDTOs, HttpStatus.OK);
    }

    @GetMapping("statistics")
    @ResponseBody
    public ResponseEntity<List<DocStatisticDTO>> getDocStatistic (@RequestParam List<Long> docIDs) {
        //TODO: We'll use a hard-coded userID for now. We'll get userID from user login token later.
        Long userID = 1L;

        List<DocStatisticDTO> docStatisticDTOs = docService.getDocStatistics(docIDs, userID);

        return new ResponseEntity<>(docStatisticDTOs, HttpStatus.OK);
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<DocDetailsDTO> postDoc (@RequestBody DocRequestDTO docRequestDTO) {
        //TODO: We'll use a hard-coded userID for now. We'll get userID from user login token later.
        Long userID = 51L;

        DocDetailsDTO docDetailsDTO = docService.createDoc(docRequestDTO, userID);

        return new ResponseEntity<>(docDetailsDTO, HttpStatus.OK);
    }

    @GetMapping("searchFilter")
    @ResponseBody
    public ResponseEntity<DocSummaryListDTO> searchFilter (
            @QuerydslPredicate(root = Doc.class) Predicate predicate,
            @RequestParam String searchTerm,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "sort", required = false) String sort,
            @PageableDefault Pageable pageable) {
        return new ResponseEntity<>(docService.getPostBySearchTerm(predicate, pageable, searchTerm), HttpStatus.OK);
    }

}
