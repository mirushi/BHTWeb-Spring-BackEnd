package com.bhtcnpm.website.controller;

import com.bhtcnpm.website.constant.ApiSortOrder;
import com.bhtcnpm.website.model.dto.Doc.*;
import com.bhtcnpm.website.model.entity.DocEntities.Doc;
import com.bhtcnpm.website.model.entity.enumeration.DocState.DocStateType;
import com.bhtcnpm.website.model.exception.FileExtensionNotAllowedException;
import com.bhtcnpm.website.service.DocService;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Request;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/documents")
@Validated
@RequiredArgsConstructor
public class DocController {

    private final DocService docService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<DocDetailsListDTO> getAllDocuments (
            @QuerydslPredicate(root = Doc.class) Predicate predicate,
            @NotNull @Min(0) Integer paginator) {
        DocDetailsListDTO result = docService.getAllDoc(predicate,paginator);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("pendingDocuments")
    @ResponseBody
    public ResponseEntity<DocSummaryListDTO> getPendingApprovalDocuments (
            @RequestParam(value = "searchTerm", required = false) String searchTerm,
            @RequestParam(value = "docState", required = false) DocStateType docState,
            @RequestParam(value = "subjectID", required = false) Long subjectID,
            @RequestParam(value = "categoryID", required = false) Long categoryID,
            @RequestParam(value = "authorID", required = false) Long authorID,
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "sortByCreatedTime", required = false) ApiSortOrder sortByCreatedTime) {
        DocSummaryListDTO result = docService.getAllPendingApprovalDoc(
                searchTerm,
                subjectID,
                categoryID,
                authorID,
                page,
                sortByCreatedTime
        );

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("myDocuments")
    @ResponseBody
    public ResponseEntity<DocSummaryWithStateListDTO> getMyDocuments (
            @RequestParam(value = "searchTerm", required = false) String searchTerm,
            @RequestParam(value = "subjectID", required = false) Long subjectID,
            @RequestParam(value = "categoryID", required = false) Long categoryID,
            @RequestParam(value = "docState", required = false) DocStateType docState,
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "sortByPublishDtm", required = false) ApiSortOrder sortByPublishDtm,
            @RequestParam(value = "sortByCreatedDtm", required = false) ApiSortOrder sortByCreatedDtm
    ) {
        //TODO: We'll use a hard-coded userID for now. We'll get userID from user login token later.
        Long userID = 1L;

        DocSummaryWithStateListDTO result = docService.getMyDocuments(
                searchTerm,
                categoryID,
                subjectID,
                docState,
                page,
                sortByPublishDtm,
                sortByCreatedDtm,
                userID
        );

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("{id}")
    @ResponseBody
    public ResponseEntity<DocDetailsDTO> putDocument (
            @PathVariable Long id,
            @RequestBody DocRequestDTO docRequestDTO) {
        //TODO: We'll use a hard-coded userID for now. We'll get userID from user login token later.
        UUID userID = DemoUserIDConstant.userID;

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
        UUID userID = DemoUserIDConstant.userID;

        List<DocStatisticDTO> docStatisticDTOs = docService.getDocStatistics(docIDs, userID);

        return new ResponseEntity<>(docStatisticDTOs, HttpStatus.OK);
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<DocDetailsDTO> postDoc (@RequestBody DocRequestDTO docRequestDTO) {
        //TODO: We'll use a hard-coded userID for now. We'll get userID from user login token later.
        UUID userID = DemoUserIDConstant.userID;

        DocDetailsDTO docDetailsDTO = docService.createDoc(docRequestDTO, userID);

        return new ResponseEntity<>(docDetailsDTO, HttpStatus.OK);
    }

    @GetMapping("searchFilter")
    @ResponseBody
    public ResponseEntity<DocSummaryListDTO> searchFilter (
            @RequestParam(value = "searchTerm", required = false) String searchTerm,
            @RequestParam(value = "categoryID", required = false) Long categoryID,
            @RequestParam(value = "subjectID", required = false) Long subjectID,
            @RequestParam(value = "authorID", required = false) Long authorID,
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "sortByPublishDtm", required = false) ApiSortOrder sortByPublishDtm) {

        DocSummaryListDTO dtoList = docService.getDocBySearchTerm(
                searchTerm,
                categoryID,
                subjectID,
                authorID,
                page,
                sortByPublishDtm
        );

        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @PostMapping("upload")
    @ResponseBody
    public ResponseEntity<DocUploadDTO> uploadDoc (@RequestParam("file")MultipartFile file) throws IOException, FileExtensionNotAllowedException {
        //TODO: We'll use a hard-coded userID for now. We'll get userID from user login token later.
        UUID userID = DemoUserIDConstant.userID;

        DocUploadDTO dto = docService.uploadFileToGDrive(file, userID);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("downloadURL")
    @ResponseBody
    public ResponseEntity<DocDownloadInfoDTO> getDownloadURL (@RequestParam("code") String fileCode) {
        DocDownloadInfoDTO dto = docService.getDocDownloadInfo(fileCode);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

        @GetMapping("getManagementDoc")
    @ResponseBody
    public ResponseEntity<DocSummaryWithStateListDTO> getManagementDoc (
            @RequestParam(value = "searchTerm", required = false) String searchTerm,
            @RequestParam(value = "subjectID", required = false) Long subjectID,
            @RequestParam(value = "categoryID", required = false) Long categoryID,
            @RequestParam(value = "authorID", required = false) Long authorID,
            @RequestParam(value = "docState", required = false) DocStateType docState,
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "sortByPublishDtm", required = false) ApiSortOrder sortByPublishDtm,
            @RequestParam(value = "sortByCreatedDtm", required = false) ApiSortOrder sortByCreatedDtm
    ) {
        DocSummaryWithStateListDTO dtoList = docService.getManagementDoc(
                searchTerm,
                categoryID,
                subjectID,
                authorID,
                docState,
                page,
                sortByPublishDtm,
                sortByCreatedDtm
        );

        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

}
