package com.bhtcnpm.website.controller.Doc;

import com.bhtcnpm.website.constant.sort.AdvancedSort;
import com.bhtcnpm.website.constant.sort.ApiSortOrder;
import com.bhtcnpm.website.model.dto.Doc.*;
import com.bhtcnpm.website.model.dto.Exercise.ExerciseAvailableActionDTO;
import com.bhtcnpm.website.model.entity.DocEntities.Doc;
import com.bhtcnpm.website.model.entity.enumeration.DocState.DocStateType;
import com.bhtcnpm.website.model.exception.FileExtensionNotAllowedException;
import com.bhtcnpm.website.model.validator.dto.Doc.DocActionRequestSize;
import com.bhtcnpm.website.model.validator.dto.Doc.DocID;
import com.bhtcnpm.website.service.Doc.DocDownloadService;
import com.bhtcnpm.website.service.Doc.DocService;
import com.bhtcnpm.website.service.Doc.DocViewService;
import com.bhtcnpm.website.util.HttpIPUtils;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/documents")
@Validated
@RequiredArgsConstructor
public class DocController {

    private final DocService docService;
    private final DocViewService docViewService;
    private final DocDownloadService docDownloadService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<DocSummaryListDTO> getAllDocuments (
            @QuerydslPredicate(root = Doc.class) Predicate predicate,
            @RequestParam(value = "mostLiked", required = false) boolean mostLiked,
            @RequestParam(value = "mostViewed", required = false) boolean mostViewed,
            @RequestParam(value = "mostDownloaded", required = false) boolean mostDownloaded,
            @PageableDefault @Nullable Pageable pageable,
            Authentication authentication) {
        DocSummaryListDTO result = docService.getAllDoc(predicate, pageable,
                mostLiked, mostViewed, mostDownloaded, authentication);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<DocDetailsDTO> getDocumentDetails (
        @PathVariable @DocID Long id,
        HttpServletRequest servletRequest,
        Authentication authentication
    ) {
        DocDetailsDTO docDetailsDTO = docService.getDocDetails(id);

        if (docDetailsDTO == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        String ipAddress = HttpIPUtils.getClientIPAddress(servletRequest);

        docViewService.addDocView(id, authentication, ipAddress);

        return new ResponseEntity<>(docDetailsDTO, HttpStatus.OK);
    }

    @GetMapping("pendingApproval")
    @ResponseBody
    public ResponseEntity<DocDetailsWithStateListDTO> getPendingApprovalDocuments (
            @QuerydslPredicate(root = Doc.class) Predicate predicate,
            @PageableDefault @Nullable Pageable pageable,
            Authentication authentication) {

        DocDetailsWithStateListDTO result = docService.getAllPendingApprovalDoc(predicate, pageable, authentication);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("myDocuments")
    @ResponseBody
    public ResponseEntity<DocSummaryWithStateAndFeedbackListDTO> getMyDocuments (
            @RequestParam(value = "searchTerm", required = false) String searchTerm,
            @RequestParam(value = "subjectID", required = false) Long subjectID,
            @RequestParam(value = "categoryID", required = false) Long categoryID,
            @RequestParam(value = "docState", required = false) DocStateType docState,
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "sortByPublishDtm", required = false) ApiSortOrder sortByPublishDtm,
            @RequestParam(value = "sortByCreatedDtm", required = false) ApiSortOrder sortByCreatedDtm,
            Authentication authentication
    ) {
        DocSummaryWithStateAndFeedbackListDTO result = docService.getMyDocuments(
                searchTerm,
                categoryID,
                subjectID,
                docState,
                page,
                sortByPublishDtm,
                sortByCreatedDtm,
                authentication
        );

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("{id}")
    @ResponseBody
    public ResponseEntity<DocDetailsDTO> putDocument (
            @PathVariable Long id,
            @RequestBody DocRequestDTO docRequestDTO,
            Authentication authentication) {
        DocDetailsDTO docDetailsDTO = docService.putDoc(id, docRequestDTO, authentication);
        return new ResponseEntity<>(docDetailsDTO, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    @ResponseBody
    public ResponseEntity deleteDoc (@PathVariable @DocID Long id,
                                     Authentication authentication) {
        Boolean result = docService.deleteDoc(id, authentication);

        if (result) {
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

//    @PostMapping
//    @ResponseBody
//    public ResponseEntity<DocDetailsDTO> postDocument (@RequestBody DocRequestDTO docRequestDTO) {
//        //TODO: We'll use a hard-coded userID for now. We'll get userID from user login token later.
//        Long userID = 1L;
//
//        DocDetailsDTO docDetailsDTO = docService.
//    }

    @GetMapping("hot")
    @ResponseBody
    public ResponseEntity<List<DocSummaryDTO>> getHotDocs (Pageable pageable,
                                                                Authentication authentication) {
        return new ResponseEntity<>(docService.getHotDocs(pageable, authentication), HttpStatus.OK);
    }

    @PostMapping("{id}/approval")
    @ResponseBody
    public ResponseEntity postDocApproval (@PathVariable Long id,
                                           Authentication authentication) {
        Boolean result = docService.postApproval(id, authentication);
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

    @PostMapping("{id}/rejection")
    @ResponseBody
    public ResponseEntity rejectDoc (@PathVariable Long id,
                                     Authentication authentication) {
        Boolean result = docService.docReject(id, authentication);

        if (result) {
            return new ResponseEntity(HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/{id}/rejectionWithFeedback")
    @ResponseBody
    public ResponseEntity postRejectionWithFeedback (@PathVariable @DocID Long id,
                                                     @RequestBody String feedback,
                                                     Authentication authentication) {
        Boolean result = docService.rejectDocWithFeedback(id, feedback);

        if (result) {
            return new ResponseEntity(HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("{id}/rejection")
    @ResponseBody
    public ResponseEntity undoRejectDoc (@PathVariable Long id,
                                         Authentication authentication) {
        Boolean result = docService.undoReject(id, authentication);

        if (result) {
            return new ResponseEntity(HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/{id}/savedStatus")
    @ResponseBody
    public ResponseEntity postSavedStatus (@PathVariable @DocID Long id,
                                           Authentication authentication) {
        Boolean result = docService.createSavedStatus(id, authentication);

        if (result) {
            return new ResponseEntity(HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(value = "/{id}/savedStatus")
    @ResponseBody
    public ResponseEntity deleteSavedStatus (@PathVariable @DocID Long id,
                                             Authentication authentication) {
        Boolean result = docService.deleteSavedStatus(id, authentication);

        if (result) {
            return new ResponseEntity(HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/savedDocs")
    @ResponseBody
    public ResponseEntity<DocSummaryListDTO> getDocSavedByUserOwn (
            @QuerydslPredicate(root = Doc.class) Predicate predicate,
            @PageableDefault @Nullable Pageable pageable,
            Authentication authentication
    ) {
        DocSummaryListDTO docSavedByUser = docService.getDocSavedByUserOwn(predicate, authentication, pageable);

        return new ResponseEntity<>(docSavedByUser, HttpStatus.OK);
    }

    @GetMapping("related")
    @ResponseBody
    public ResponseEntity<List<DocSuggestionDTO>> getRelatedDocs (@RequestParam(value = "postID", required = false) Long postID,
                                                                  @RequestParam(value = "docID", required = false) Long docID,
                                                                  @RequestParam(value = "exerciseID", required = false) Long exerciseID,
                                                                  @RequestParam(value = "authorID", required = false) UUID authorID,
                                                                  @RequestParam(value = "categoryID", required = false) Long categoryID,
                                                                  @RequestParam(value = "subjectID", required = false) Long subjectID,
                                                                  @RequestParam(value = "page", required = false) Integer page,
                                                                  Authentication authentication) throws IOException {
        List<DocSuggestionDTO> docSuggestionDTOs = docService
                .getRelatedDocs(postID, docID, exerciseID, authorID, categoryID, subjectID, page, authentication);

        return new ResponseEntity<>(docSuggestionDTOs, HttpStatus.OK);
    }

    @GetMapping("statistics")
    @ResponseBody
    public ResponseEntity<List<DocStatisticDTO>> getDocStatistic (@RequestParam List<Long> docIDs,
                                                                      Authentication authentication) {
        List<DocStatisticDTO> docStatisticDTOs = docService.getDocStatistics(docIDs, authentication);

        return new ResponseEntity<>(docStatisticDTOs, HttpStatus.OK);
    }

    @GetMapping("/actionAvailable")
    @ResponseBody
    public ResponseEntity<List<DocAvailableActionDTO>> getDocActionAvailable (
            @RequestParam @DocActionRequestSize List<Long> docIDs,
            Authentication authentication
    ) {
        List<DocAvailableActionDTO> availableActionDTOList = docService.getAvailableDocAction(docIDs, authentication);

        return new ResponseEntity<>(availableActionDTOList, HttpStatus.OK);
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<DocDetailsDTO> postDoc (@RequestBody DocRequestDTO docRequestDTO,
                                                  Authentication authentication) {
        DocDetailsDTO docDetailsDTO = docService.createDoc(docRequestDTO, authentication);

        return new ResponseEntity<>(docDetailsDTO, HttpStatus.OK);
    }

    @PostMapping("image")
    @ResponseBody
    public ResponseEntity<String> uploadImage (@RequestParam("file") MultipartFile file,
                                               Authentication authentication) throws FileExtensionNotAllowedException, IOException {
        String imageURL = docService.uploadImage(file, authentication);

        return new ResponseEntity<>(imageURL, HttpStatus.OK);
    }

    @GetMapping("searchFilter")
    @ResponseBody
    public ResponseEntity<DocSummaryListDTO> searchFilter (
            @RequestParam(value = "searchTerm", required = false) String searchTerm,
            @RequestParam(value = "categoryID", required = false) Long categoryID,
            @RequestParam(value = "subjectID", required = false) Long subjectID,
            @RequestParam(value = "authorID", required = false) UUID authorID,
            @RequestParam(value = "tags", required = false) Long tagID,
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "sortByPublishDtm", required = false) ApiSortOrder sortByPublishDtm,
            @RequestParam(value = "advancedSort", required = false) AdvancedSort advancedSort,
            Authentication authentication) {

        DocSummaryListDTO dtoList = docService.getDocBySearchTerm(
                searchTerm,
                categoryID,
                subjectID,
                authorID,
                tagID,
                page,
                sortByPublishDtm,
                advancedSort,
                authentication
        );

        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @PostMapping("upload")
    @ResponseBody
    public ResponseEntity<DocFileUploadDTO> uploadDoc (@RequestParam("file")MultipartFile file,
                                                       Authentication authentication) throws IOException, FileExtensionNotAllowedException {
        DocFileUploadDTO dto = docService.uploadFileToGDrive(file, authentication);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("downloadURL")
    @ResponseBody
    public ResponseEntity<DocDownloadInfoDTO> getDownloadURL (@RequestParam("id") UUID fileID,
                                                              HttpServletRequest servletRequest,
                                                              Authentication authentication) {
        DocDownloadInfoDTO dto = docService.getDocDownloadInfo(fileID);

        if (dto == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        String ipAddress = HttpIPUtils.getClientIPAddress(servletRequest);

        docDownloadService.addDocDownload(fileID, authentication, ipAddress);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("getManagementDoc")
    @ResponseBody
    public ResponseEntity<DocSummaryWithStateListDTO> getManagementDoc (
            @RequestParam(value = "searchTerm", required = false) String searchTerm,
            @RequestParam(value = "subjectID", required = false) Long subjectID,
            @RequestParam(value = "categoryID", required = false) Long categoryID,
            @RequestParam(value = "authorID", required = false) UUID authorID,
            @RequestParam(value = "docState", required = false) DocStateType docState,
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "sortByPublishDtm", required = false) ApiSortOrder sortByPublishDtm,
            @RequestParam(value = "sortByCreatedDtm", required = false) ApiSortOrder sortByCreatedDtm,
            Authentication authentication
    ) {
        DocSummaryWithStateListDTO dtoList = docService.getManagementDoc(
                searchTerm,
                categoryID,
                subjectID,
                authorID,
                docState,
                page,
                sortByPublishDtm,
                sortByCreatedDtm,
                authentication
        );

        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }
}
