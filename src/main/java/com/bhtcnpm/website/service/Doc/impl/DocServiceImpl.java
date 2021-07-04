package com.bhtcnpm.website.service.Doc.impl;

import com.bhtcnpm.website.constant.business.Doc.AllowedUploadExtension;
import com.bhtcnpm.website.constant.business.Doc.DocFileUploadConstant;
import com.bhtcnpm.website.constant.domain.Doc.DocBusinessState;
import com.bhtcnpm.website.constant.security.evaluator.permission.DocActionPermissionRequest;
import com.bhtcnpm.website.constant.sort.ApiSortOrder;
import com.bhtcnpm.website.model.dto.Doc.*;
import com.bhtcnpm.website.model.dto.Doc.mapper.*;
import com.bhtcnpm.website.model.dto.Exercise.ExerciseDetailsDTO;
import com.bhtcnpm.website.model.dto.Post.PostDetailsDTO;
import com.bhtcnpm.website.model.entity.DocEntities.Doc;
import com.bhtcnpm.website.model.entity.DocEntities.DocFileUpload;
import com.bhtcnpm.website.model.entity.DocEntities.UserDocSave;
import com.bhtcnpm.website.model.entity.DocEntities.UserDocSaveId;
import com.bhtcnpm.website.model.entity.Tag;
import com.bhtcnpm.website.model.entity.UserWebsite;
import com.bhtcnpm.website.model.entity.enumeration.DocState.DocStateType;
import com.bhtcnpm.website.model.exception.FileExtensionNotAllowedException;
import com.bhtcnpm.website.repository.Doc.DocFileUploadRepository;
import com.bhtcnpm.website.repository.Doc.DocRepository;
import com.bhtcnpm.website.repository.Doc.UserDocReactionRepository;
import com.bhtcnpm.website.repository.Doc.UserDocSaveRepository;
import com.bhtcnpm.website.repository.TagRepository;
import com.bhtcnpm.website.repository.UserWebsiteRepository;
import com.bhtcnpm.website.security.evaluator.Doc.DocPermissionEvaluator;
import com.bhtcnpm.website.security.predicate.Doc.DocPredicateGenerator;
import com.bhtcnpm.website.security.predicate.Doc.UserDocSavePredicateGenerator;
import com.bhtcnpm.website.security.util.SecurityUtils;
import com.bhtcnpm.website.service.Doc.DocFileUploadService;
import com.bhtcnpm.website.service.Doc.DocService;
import com.bhtcnpm.website.service.Exercise.ExerciseService;
import com.bhtcnpm.website.service.GoogleDriveService;
import com.bhtcnpm.website.service.Post.PostService;
import com.bhtcnpm.website.service.util.PaginatorUtils;
import com.bhtcnpm.website.util.EnumConverter;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
@RequiredArgsConstructor
public class DocServiceImpl implements DocService {

    private static final int PAGE_SIZE = 10;

    private static final int PAGE_SIZE_RELATED_DOC = 3;

    private static final int PAGE_SIZE_TRENDING_DOC = 16;

    private static final int PAGE_SIZE_HOT_DOC = 16;

    private static final int FILE_NAME_RANDOM_LENGTH = 10;

    private static final String DRIVE_UPLOAD_DEFAULT_FOLDER_ID = "1mg_iZfewkU93WhcFfKYf38irvW1Gr-wn";

    private final DocDetailsMapper docDetailsMapper;

    private final DocSummaryMapper docSummaryMapper;

    private final DocRequestMapper docRequestMapper;

    private final DocSuggestionMapper docSuggestionMapper;

    private final DocDownloadInfoMapper docDownloadInfoMapper;

    private final DocFileUploadMapper docFileUploadMapper;

    private final DocRepository docRepository;

    private final DocFileUploadRepository docFileUploadRepository;

    private final TagRepository tagRepository;

    private final UserDocReactionRepository userDocReactionRepository;

    private final UserDocSaveRepository userDocSaveRepository;

    private final UserWebsiteRepository userWebsiteRepository;

    private final DocFileUploadService docFileUploadService;

    private final DocPermissionEvaluator docPermissionEvaluator;

    private final PostService postService;

    private final ExerciseService exerciseService;

    public DocSummaryListDTO getAllDoc (Predicate predicate, Pageable pageable, Authentication authentication) {

        //Create a pagable.
        pageable = PaginatorUtils.getPageableWithNewPageSizeAndMoreSort(pageable, PAGE_SIZE, Sort.by("publishDtm").descending());

        BooleanExpression publicDocFilter = DocPredicateGenerator.getBooleanExpressionOnBusinessState(DocBusinessState.PUBLIC);
        BooleanExpression authorizationFilter = DocPredicateGenerator.getBooleanExpressionOnAuthentication(authentication);

        Page<Doc> queryResult = docRepository.findAll(publicDocFilter.and(authorizationFilter).and(predicate), pageable);

        List<DocSummaryDTO> docSummaryDTOs= StreamSupport
                .stream(queryResult.spliterator(), false)
                .map(docSummaryMapper::docToDocSummaryDTO)
                .collect(Collectors.toList());

        return new DocSummaryListDTO(docSummaryDTOs, queryResult.getTotalPages(), queryResult.getTotalElements());
    }

    @Override
    public DocDetailsWithStateListDTO getAllPendingApprovalDoc(
            Predicate predicate,
            Pageable pageable,
            Authentication authentication
    ) {
        //Create a pagable.
        BooleanExpression authorizationFilter = DocPredicateGenerator.getBooleanExpressionOnAuthentication(authentication);
        BooleanExpression docBusinessState = DocPredicateGenerator.getBooleanExpressionNotDeleted();
        BooleanExpression docState = DocPredicateGenerator.getBooleanExpressionOnDocStateType(DocStateType.PENDING_APPROVAL);

        Page<Doc> docPage = docRepository.findAll(authorizationFilter.and(docBusinessState).and(docState).and(predicate), pageable);

        return docDetailsMapper.docPageToDocDetailsWithStateListDTO(docPage);
    }

    @Override
    public DocSummaryWithStateListDTO getMyDocuments(String searchTerm,
                                            Long categoryID,
                                            Long subjectID,
                                            DocStateType docState,
                                            Integer page,
                                            ApiSortOrder sortByPublishDtm,
                                            ApiSortOrder sortByCreatedDtm,
                                            Authentication authentication) {
        UUID userID = SecurityUtils.getUserIDOnNullThrowException(authentication);

        DocSummaryWithStateListDTO dtoList = docRepository.getMyDocSummaryWithStateList(
                searchTerm,
                null,
                categoryID,
                subjectID,
                userID,
                docState,
                page,
                PAGE_SIZE,
                EnumConverter.apiSortOrderToHSearchSortOrder(sortByPublishDtm),
                EnumConverter.apiSortOrderToHSearchSortOrder(sortByCreatedDtm)
        );

        return dtoList;
    }

    @Override
    public DocDetailsDTO putDoc(Long docID, DocRequestDTO docRequestDTO, Authentication authentication) {
        UUID userID = SecurityUtils.getUserIDOnNullThrowException(authentication);

        Doc oldDoc = null;

        if (docID != null) {
            Optional<Doc> oldDocQuery = docRepository.findById(docID);
            if (oldDocQuery.isPresent()) {
                oldDoc = oldDocQuery.get();
            }
        }

        List<DocFileUpload> fileUploadList = docFileUploadService.getFileUploadOwnerOnly(
                docRequestDTO.getDocFileUploadRequestDTOs().stream().map(DocFileUploadRequestDTO::getId).collect(Collectors.toList()),
                authentication
        );
        fileUploadList = docFileUploadMapper.updateDocFileUpload(fileUploadList, docRequestDTO.getDocFileUploadRequestDTOs());

        Doc doc = docRequestMapper.updateDocFromDocRequestDTO(docRequestDTO, oldDoc, fileUploadList ,userID);

        doc = docRepository.save(doc);

        return docDetailsMapper.docToDocDetailsDTO(doc);
    }

    @Override
    public Boolean postApproval(Long docID, Authentication authentication) {
        //TODO: When approve, also move file(s) to approved folder in Google Drive.
        int rowChanged = docRepository.setDocState(docID, DocStateType.APPROVED);
        if (rowChanged == 1) {
            docRepository.indexDoc(docID);
            return true;
        }
        return false;
    }

    @Override
    public Boolean deleteApproval (Long docID) {
        int rowChanged = docRepository.setDocState(docID, DocStateType.PENDING_APPROVAL);
        if (rowChanged == 1) {
            docRepository.indexDoc(docID);
            return true;
        }
        return false;
    }

    @Override
    public Boolean increaseDownloadCount(Long docID, Long userID) {
        //TODO: Please check condition before increase download count;
//        int rowChanged = docRepository.incrementDownloadCount(docID);
//        if (rowChanged == 1) {
//            return true;
//        }
        return false;
    }

    @Override
    public Boolean docReject(Long docID, Authentication authentication) {
        int rowChanged = docRepository.setDocState(docID, DocStateType.REJECTED);
        if (rowChanged == 1) {
            docRepository.indexDoc(docID);
            return true;
        }

        return false;
    }

    @Override
    public Boolean undoReject(Long docID, Authentication authentication) {
        int rowChanged = docRepository.setDocState(docID, DocStateType.PENDING_APPROVAL);
        if (rowChanged == 1) {
            docRepository.indexDoc(docID);
            return true;
        }

        return false;
    }

    @Override
    public Boolean createSavedStatus(Long docID, Authentication authentication) {
        UUID userID = SecurityUtils.getUserIDOnNullThrowException(authentication);
        if (userID == null) {
            throw new IllegalArgumentException("UserID not found. Cannot perform save.");
        }

        UserDocSaveId id = new UserDocSaveId();
        id.setDoc(docRepository.getOne(docID));
        id.setUser(userWebsiteRepository.getOne(userID));
        UserDocSave userDocSave = new UserDocSave();
        userDocSave.setUserDocSaveId(id);

        userDocSaveRepository.save(userDocSave);

        return true;
    }

    @Override
    public Boolean deleteSavedStatus(Long docID, Authentication authentication) {
        UUID userID = SecurityUtils.getUserIDOnNullThrowException(authentication);

        UserDocSaveId id = new UserDocSaveId();
        id.setDoc(docRepository.getOne(docID));
        id.setUser(userWebsiteRepository.getOne(userID));

        userDocSaveRepository.deleteById(id);

        return true;
    }

    @Override
    public DocSummaryListDTO getDocSavedByUserOwn(Predicate predicate, Authentication authentication, Pageable pageable) {
        BooleanExpression authorizationFilter = UserDocSavePredicateGenerator.getBooleanExpressionOnAuthentication(authentication);
        BooleanExpression docPublicBusinessStateFilter = UserDocSavePredicateGenerator.getBooleanExpressionOnDocBusinessState(DocBusinessState.PUBLIC);
        BooleanExpression userOwnFilter = UserDocSavePredicateGenerator.getBooleanExpressionUserOwn(authentication);

        Predicate finalPredicate = authorizationFilter.and(docPublicBusinessStateFilter).and(userOwnFilter).and(predicate);

        //Reset PAGE_SIZE to predefined value.
        pageable = PaginatorUtils.getPageableWithNewPageSize(pageable, PAGE_SIZE);

        Page<UserDocSave> userDocSaves = userDocSaveRepository.findAll(finalPredicate, pageable);

        return docSummaryMapper.userDocSavePageToDocSummaryListDTO(userDocSaves);
    }

    @Override
    public List<DocSuggestionDTO> getRelatedDocs(Long postID, Long docID, Long exerciseID,
                                                 UUID authorID, Long categoryID, Long subjectID,
                                                 Integer page, Authentication authentication) throws IOException {
        if (page == null) {
            page = 0;
        }
        assertExactlyOneParamIsNotNull(postID, docID, exerciseID);

        Long currentDocID = null;
        String title = null;
        String description = null;

        if (postID != null) {
            PostDetailsDTO postDetailsDTO = postService.getPostDetails(postID);
            title = postDetailsDTO.getTitle();
            description = postDetailsDTO.getSummary();
        } else if (docID != null) {
            if (!docPermissionEvaluator.hasPermission(authentication, docID, DocActionPermissionRequest.READ_PERMISSION)) {
                throw new AccessDeniedException("You do not have permission to access this DocID.");
            }
            Optional<Doc> doc = docRepository.findById(docID);
            if (doc.isEmpty()) {
                throw new IllegalArgumentException("DocID not found.");
            }
            currentDocID = docID;
            title = doc.get().getTitle();
            description = doc.get().getDescription();
        } else if (exerciseID != null) {
            ExerciseDetailsDTO exerciseDetailsDTO = exerciseService.getExerciseDetails(exerciseID);
            title = exerciseDetailsDTO.getTitle();
            description = exerciseDetailsDTO.getDescription();
        }

        List<DocSuggestionDTO> searchRelatedDocs = docRepository.searchRelatedDocs(authorID, categoryID, subjectID, currentDocID,
                title, description, page, PAGE_SIZE_RELATED_DOC, DocBusinessState.PUBLIC, authentication);

        return searchRelatedDocs;
    }

    @Override
    public List<DocSummaryDTO> getHotDocs(Pageable pageable, Authentication authentication) {
        pageable = PaginatorUtils.getPageableWithNewPageSize(pageable, PAGE_SIZE_HOT_DOC);

        return docSummaryMapper.docListToDocSummaryDTOList(docRepository.getHotDocsPublicOnly(pageable));
    }

    @Override
    public List<DocStatisticDTO> getDocStatistics(List<Long> docIDs, Authentication authentication) {
        UUID userID = SecurityUtils.getUserID(authentication);
        List<DocStatisticDTO> docStatisticDTOList = docRepository.getDocStatisticDTOs(docIDs, userID);

        return docStatisticDTOList;
    }

    @Override
    public DocDetailsDTO createDoc(DocRequestDTO docRequestDTO, Authentication authentication) {
        UUID userID = SecurityUtils.getUserIDOnNullThrowException(authentication);

        List<DocFileUpload> fileUploadList = docFileUploadService.getFileUploadOwnerOnly(
                docRequestDTO.getDocFileUploadRequestDTOs().stream().map(DocFileUploadRequestDTO::getId).collect(Collectors.toList()),
                authentication
        );
        fileUploadList = docFileUploadMapper.updateDocFileUpload(fileUploadList, docRequestDTO.getDocFileUploadRequestDTOs());

        Doc doc = docRequestMapper.updateDocFromDocRequestDTO(docRequestDTO, null, fileUploadList, userID);

        doc = docRepository.save(doc);

        return docDetailsMapper.docToDocDetailsDTO(doc);
    }

    @Override
    public Boolean deleteDoc(Long docID, Authentication authentication) {
        docRepository.deleteById(docID);
        return true;
    }

    @Override
    public DocSummaryListDTO getDocBySearchTerm(
            String searchTerm,
            Long categoryID,
            Long subjectID,
            UUID authorID,
            Long tagID,
            Integer page,
            ApiSortOrder sortByPublishDtm,
            Authentication authentication
    ) {
        //TODO: DocState depends on ACL.

        String tagContent = null;
        if (tagID != null){
            Optional<Tag> object = tagRepository.findById(tagID);
            if (object.isPresent()) {
                tagContent = object.get().getContent();
            }
        }

        DocSummaryListDTO queryResult = docRepository.getDocSummaryList(
                searchTerm,
                tagContent,
                categoryID,
                subjectID,
                authorID,
                null,
                page,
                PAGE_SIZE,
                EnumConverter.apiSortOrderToHSearchSortOrder(sortByPublishDtm),
                null,
                authentication);

        return queryResult;
    }

    @Override
    public DocDetailsDTO getDocDetails(Long id) {
        Optional<Doc> doc = docRepository.getAllFilterWithTagsAndDocFileUploadsById(id);

        if (doc.isEmpty()) {
            return null;
        }

        return docDetailsMapper.docToDocDetailsDTO(doc.get());
    }

    @Override
    public DocFileUploadDTO uploadFileToGDrive(MultipartFile multipartFile, Authentication authentication) throws IOException, FileExtensionNotAllowedException {
        //TODO: Limit how much user can upload (aka rate limiting).
        UUID userID = SecurityUtils.getUserIDOnNullThrowException(authentication);

        byte[] fileContent = multipartFile.getBytes();
        String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());

        //Get mime type.
        String mimeType = AllowedUploadExtension.getMimeType(extension);

        //Generate random file name (for security reason, don't trust user submitted file name.
        //https://cheatsheetseries.owasp.org/cheatsheets/File_Upload_Cheat_Sheet.html#file-upload-threats
        String fileName = RandomStringUtils.randomAlphanumeric(FILE_NAME_RANDOM_LENGTH) + "." + extension;

        //Get subfolder of userID to upload file.
        com.google.api.services.drive.model.File uploadedFile = GoogleDriveService
            .createGoogleFileWithUserID(DRIVE_UPLOAD_DEFAULT_FOLDER_ID, userID, mimeType, fileName, fileContent);

        //Get proxy for author.
        UserWebsite author = userWebsiteRepository.getOne(userID);

        //Save DB location for the file.
        DocFileUpload fileUpload = DocFileUpload.builder()
            .fileName(multipartFile.getOriginalFilename())
            .fileSize(multipartFile.getSize())
            .downloadURL(uploadedFile.getWebViewLink())
            .thumbnailURL(String.format(DocFileUploadConstant.DRIVE_THUMBNAIL_URL, uploadedFile.getId()))
            .uploader(author).remoteID(uploadedFile.getId()).rank(-1).doc(null)
            .build();

        fileUpload = docFileUploadRepository.save(fileUpload);

        return DocFileUploadDTO.builder()
                .fileName(fileUpload.getFileName())
                .id(fileUpload.getId())
                .fileSize(multipartFile.getSize())
                .build();
    }

    @Override
    public DocDownloadInfoDTO getDocDownloadInfo(UUID fileID) {
        //TODO: Add download increment info.
        Optional<DocFileUpload> fileObject = docFileUploadRepository.findById(fileID);

        if (fileObject.isEmpty()) {
            throw new IllegalArgumentException("File not found.");
        }

        return docDownloadInfoMapper.docFileUploadToDocDownloadInfoDTO(fileObject.get());
    }

    @Override
    public DocSummaryWithStateListDTO getManagementDoc(
            String searchTerm,
            Long categoryID,
            Long subjectID,
            UUID authorID,
            DocStateType docState,
            Integer page,
            ApiSortOrder sortByPublishDtm,
            ApiSortOrder sortByCreatedDtm,
            Authentication authentication
    ) {
        DocSummaryWithStateListDTO dtoList = docRepository.getDocSummaryWithStateList(
                searchTerm,
                null,
                categoryID,
                subjectID,
                authorID,
                docState,
                page,
                PAGE_SIZE,
                EnumConverter.apiSortOrderToHSearchSortOrder(sortByPublishDtm),
                EnumConverter.apiSortOrderToHSearchSortOrder(sortByCreatedDtm),
                authentication
        );
        return dtoList;
    }

    private void assertExactlyOneParamIsNotNull(Object... params) {
        final String multipleParamViolation = "Only one param is allowed at a time.";
        final String noParamViolation = "Exactly one param must not be null.";

        boolean isNotNullParamExist = false;
        for (int i=0; i<params.length; ++i) {
            Object currentParam = params[i];
            if (currentParam != null && isNotNullParamExist) {
                throw new IllegalArgumentException(multipleParamViolation);
            } else if (currentParam != null) {
                isNotNullParamExist = true;
            }
        }
        if (!isNotNullParamExist) {
            throw new IllegalArgumentException(noParamViolation);
        }
    }

}
