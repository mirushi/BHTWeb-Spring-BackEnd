package com.bhtcnpm.website.service.Doc.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.bhtcnpm.website.constant.business.Doc.AllowedUploadExtension;
import com.bhtcnpm.website.constant.business.Doc.DocActionAvailableConstant;
import com.bhtcnpm.website.constant.business.Doc.DocFileUploadConstant;
import com.bhtcnpm.website.constant.business.GenericBusinessConstant;
import com.bhtcnpm.website.constant.domain.Doc.DocBusinessState;
import com.bhtcnpm.website.constant.security.evaluator.permission.DocActionPermissionRequest;
import com.bhtcnpm.website.constant.sort.AdvancedSort;
import com.bhtcnpm.website.constant.sort.ApiSortOrder;
import com.bhtcnpm.website.model.dto.AWS.AmazonS3ResultDTO;
import com.bhtcnpm.website.model.dto.Doc.*;
import com.bhtcnpm.website.model.dto.Doc.mapper.*;
import com.bhtcnpm.website.model.dto.Exercise.ExerciseDetailsDTO;
import com.bhtcnpm.website.model.dto.Post.PostDetailsDTO;
import com.bhtcnpm.website.model.entity.DocEntities.Doc;
import com.bhtcnpm.website.model.entity.DocEntities.DocFileUpload;
import com.bhtcnpm.website.model.entity.DocEntities.UserDocSave;
import com.bhtcnpm.website.model.entity.DocEntities.UserDocSaveId;
import com.bhtcnpm.website.model.entity.ExerciseEntities.Exercise;
import com.bhtcnpm.website.model.entity.Tag;
import com.bhtcnpm.website.model.entity.UserWebsite;
import com.bhtcnpm.website.model.entity.enumeration.DocFileUpload.DocFileUploadHostType;
import com.bhtcnpm.website.model.entity.enumeration.DocReaction.DocReactionType;
import com.bhtcnpm.website.model.entity.enumeration.DocState.DocStateType;
import com.bhtcnpm.website.model.entity.enumeration.UserWebsite.ReputationType;
import com.bhtcnpm.website.model.exception.FileExtensionNotAllowedException;
import com.bhtcnpm.website.repository.Doc.*;
import com.bhtcnpm.website.repository.TagRepository;
import com.bhtcnpm.website.repository.UserWebsiteRepository;
import com.bhtcnpm.website.security.evaluator.Doc.DocPermissionEvaluator;
import com.bhtcnpm.website.security.predicate.Doc.DocPredicateGenerator;
import com.bhtcnpm.website.security.predicate.Doc.UserDocSavePredicateGenerator;
import com.bhtcnpm.website.security.util.SecurityUtils;
import com.bhtcnpm.website.service.Doc.DocFileUploadService;
import com.bhtcnpm.website.service.Doc.DocService;
import com.bhtcnpm.website.service.Exercise.ExerciseService;
import com.bhtcnpm.website.service.FileUploadService;
import com.bhtcnpm.website.service.GoogleDriveService;
import com.bhtcnpm.website.service.Post.PostService;
import com.bhtcnpm.website.service.UserWebsiteService;
import com.bhtcnpm.website.service.util.PaginatorUtils;
import com.bhtcnpm.website.util.EnumConverter;
import com.bhtcnpm.website.util.FileUploadUtils;
import com.bhtcnpm.website.util.ValidationUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.jpa.TypedParameterValue;
import org.hibernate.type.PostgresUUIDType;
import org.jsoup.helper.Validate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
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

    private final DocSearchMapper docSearchMapper;

    private final DocRepository docRepository;

    private final DocViewRepository docViewRepository;

    private final DocDownloadRepository docDownloadRepository;

    private final DocFileUploadRepository docFileUploadRepository;

    private final TagRepository tagRepository;

    private final UserDocReactionRepository userDocReactionRepository;

    private final UserDocSaveRepository userDocSaveRepository;

    private final UserWebsiteRepository userWebsiteRepository;

    private final UserWebsiteService userWebsiteService;

    private final DocFileUploadService docFileUploadService;

    private final FileUploadService fileUploadService;

    private final DocPermissionEvaluator docPermissionEvaluator;

    private final PostService postService;

    private final ExerciseService exerciseService;

    @Override
    public DocSummaryListDTO getAllDoc (Predicate predicate, Pageable pageable,boolean mostLiked, boolean mostViewed, boolean mostDownloaded, Authentication authentication) {

        ValidationUtils.assertAtMostOneParamIsTrue(mostLiked, mostViewed, mostDownloaded);

        //Create a pagable.
        pageable = PaginatorUtils.getPageableWithNewPageSizeAndMoreSort(pageable, PAGE_SIZE, Sort.by("publishDtm").descending());

        BooleanExpression publicDocFilter = DocPredicateGenerator.getBooleanExpressionOnBusinessState(DocBusinessState.PUBLIC);
        BooleanExpression authorizationFilter = DocPredicateGenerator.getBooleanExpressionOnAuthentication(authentication);
        BooleanExpression finalPredicate = authorizationFilter.and(publicDocFilter).and(predicate);

        Page<Doc> queryResult;

        if (mostLiked) {
            queryResult = docRepository.getDocOrderByLikeCountDESC(finalPredicate, pageable);
        } else if (mostViewed) {
            queryResult = docRepository.getDocOrderByViewCountDESC(finalPredicate, pageable);
        } else if (mostDownloaded) {
            queryResult = docRepository.getDocOrderByDownloadCountDESC(finalPredicate, pageable);
        } else {
            queryResult = docRepository.findAll(finalPredicate, pageable);
        }

        List<DocSummaryDTO> docSummaryDTOs = StreamSupport
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
    public DocSummaryWithStateAndFeedbackListDTO getMyDocuments(String searchTerm,
                                            Long categoryID,
                                            Long subjectID,
                                            DocStateType docState,
                                            Integer page,
                                            ApiSortOrder sortByPublishDtm,
                                            ApiSortOrder sortByCreatedDtm,
                                            Authentication authentication) {
        UUID userID = SecurityUtils.getUserIDOnNullThrowException(authentication);

        DocSummaryWithStateAndFeedbackListDTO dtoList = docRepository.getMyDocSummaryWithStateList(
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

        List<DocFileUpload> fileUploadList = docFileUploadService.filterFileUploadForDoc(
                docRequestDTO.getDocFileUploadRequestDTOs().stream().map(DocFileUploadRequestDTO::getId).collect(Collectors.toList()),
                (oldDoc != null) ? (oldDoc.getId()) : (null),
                authentication
        );
        fileUploadList = docFileUploadMapper.updateDocFileUpload(fileUploadList, docRequestDTO.getDocFileUploadRequestDTOs());

        Doc doc = docRequestMapper.updateDocFromDocRequestDTO(docRequestDTO, oldDoc, fileUploadList ,userID);

        if (DocStateType.PENDING_FIX.equals(doc.getDocState())) {
            doc.setDocState(DocStateType.PENDING_APPROVAL);
        }

        doc = docRepository.save(doc);

        return docDetailsMapper.docToDocDetailsDTO(doc);
    }

    @Override
    public Boolean postApproval(Long docID, Authentication authentication) {
        //TODO: When approve, also move file(s) to approved folder in Google Drive.
        int rowChanged = docRepository.setDocStateAndFeedback(docID, DocStateType.APPROVED, null);
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
    public Boolean rejectDocWithFeedback(Long docID, String feedback) {
        int rowChanged = docRepository.setDocStateAndFeedback(docID, DocStateType.PENDING_FIX, feedback);

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
        ValidationUtils.assertExactlyOneParamIsNotNull(postID, docID, exerciseID);

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
        TypedParameterValue userIDParam = new TypedParameterValue(new PostgresUUIDType(), userID);
        List<DocStatisticDTO> docStatisticDTOList = docRepository.getDocStatisticDTOs(docIDs, userIDParam);

        return docStatisticDTOList;
    }

    @Override
    public DocDetailsDTO createDoc(DocRequestDTO docRequestDTO, Authentication authentication) {
        UUID userID = SecurityUtils.getUserIDOnNullThrowException(authentication);

        List<DocFileUpload> fileUploadList = docFileUploadService.filterFileUploadForDoc(
                docRequestDTO.getDocFileUploadRequestDTOs().stream().map(DocFileUploadRequestDTO::getId).collect(Collectors.toList()),
                null,
                authentication
        );
        fileUploadList = docFileUploadMapper.updateDocFileUpload(fileUploadList, docRequestDTO.getDocFileUploadRequestDTOs());

        Doc doc = docRequestMapper.updateDocFromDocRequestDTO(docRequestDTO, null, fileUploadList, userID);

        doc = docRepository.save(doc);

        return docDetailsMapper.docToDocDetailsDTO(doc);
    }

    @Override
    public String uploadImage(MultipartFile multipartFile, Authentication authentication) throws FileExtensionNotAllowedException, IOException {
        UUID userID = SecurityUtils.getUserIDOnNullThrowException(authentication);

        String key = FileUploadUtils.getS3DocImageURLUploadKey(userID, multipartFile);

        AmazonS3ResultDTO result = fileUploadService.uploadImageToS3(key, multipartFile);

        String imageURL = result.getDirectURL();

        return imageURL;
    }

    @Override
    public Boolean deleteDoc(Long docID, Authentication authentication) {
        Optional<Doc> docOpt = docRepository.findById(docID);
        Validate.isTrue(docOpt.isPresent(), String.format("Doc with id = %s cannot be found.", docID));
        Doc docEntity = docOpt.get();
        UUID authorID = docEntity.getAuthor().getId();

        //Perform reputation reset.
        long totalLike = userDocReactionRepository.countByDocReactionTypeAndUserDocReactionIdDocId(DocReactionType.LIKE, docID);
        long totalDislike = userDocReactionRepository.countByDocReactionTypeAndUserDocReactionIdDocId(DocReactionType.DISLIKE, docID);

        userWebsiteService.subtractUserReputationScore(authorID, ReputationType.DOC_LIKED, totalLike);
        userWebsiteService.subtractUserReputationScore(authorID, ReputationType.DOC_DISLIKED, totalDislike);

        docRepository.delete(docEntity);

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
            AdvancedSort advancedSort,
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
                advancedSort,
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

        com.google.api.services.drive.model.File uploadedFile = fileUploadService.uploadToGoogleDrive(multipartFile, DRIVE_UPLOAD_DEFAULT_FOLDER_ID, authentication);

        //Get proxy for author.
        UserWebsite author = userWebsiteRepository.getOne(userID);

        //Save DB location for the file.
        DocFileUpload fileUpload = DocFileUpload.builder()
            .fileName(multipartFile.getOriginalFilename())
            .fileSize(multipartFile.getSize())
            .downloadURL(uploadedFile.getWebViewLink())
            .thumbnailURL(String.format(DocFileUploadConstant.DRIVE_THUMBNAIL_URL, uploadedFile.getId()))
            .uploader(author)
            .remoteID(uploadedFile.getId()).rank(-1).doc(null).hostType(DocFileUploadHostType.G_DRIVE)
            .build();

        fileUpload = docFileUploadRepository.save(fileUpload);

        return docFileUploadMapper.docFileUploadToDocFileUploadDTO(fileUpload);
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
    public List<DocAvailableActionDTO> getAvailableDocAction(List<Long> docIDs, Authentication authentication) {
        List<DocAvailableActionDTO> docAvailableActionDTOList = new ArrayList<>();

        for (Long docID : docIDs) {
            if (docID == null) {
                continue;
            }
            DocAvailableActionDTO docAvailableActionDTO = new DocAvailableActionDTO();
            docAvailableActionDTO.setId(docID);
            List<String> availableAction = new ArrayList<>();

            if (docPermissionEvaluator.hasPermission(authentication, docID, DocActionPermissionRequest.READ_PERMISSION)) {
                availableAction.add(DocActionAvailableConstant.READ_ACTION);
            }

            if (docPermissionEvaluator.hasPermission(authentication, docID, DocActionPermissionRequest.UPDATE_PERMISSION)) {
                availableAction.add(DocActionAvailableConstant.UPDATE_ACTION);
            }

            if (docPermissionEvaluator.hasPermission(authentication, docID, DocActionPermissionRequest.DELETE_PERMISSION)) {
                availableAction.add(DocActionAvailableConstant.DELETE_ACTION);
            }

            if (docPermissionEvaluator.hasPermission(authentication, docID, DocActionPermissionRequest.SAVE_PERMISSION)) {
                availableAction.add(DocActionAvailableConstant.SAVE_ACTION);
            }

            if (docPermissionEvaluator.hasPermission(authentication, docID, DocActionPermissionRequest.REACT_PERMISSION)) {
                availableAction.add(DocActionAvailableConstant.REACT_ACTION);
            }

            if (docPermissionEvaluator.hasPermission(authentication, docID, DocActionPermissionRequest.APPROVE_PERMISSION)) {
                availableAction.add(DocActionAvailableConstant.APPROVE_ACTION);
            }
            if (docPermissionEvaluator.hasPermission(authentication, docID, DocActionPermissionRequest.REPORT_PERMISSION)) {
                availableAction.add(DocActionAvailableConstant.REPORT_ACTION);
            }
            if (docPermissionEvaluator.hasPermission(authentication, docID, DocActionPermissionRequest.COMMENT_PERMISSION)) {
                availableAction.add(DocActionAvailableConstant.COMMENT_ACTION);
            }
            docAvailableActionDTO.setAvailableActions(availableAction);

            docAvailableActionDTOList.add(docAvailableActionDTO);
        }

        return docAvailableActionDTOList;
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

    @Override
    public List<DocQuickSearchResult> quickSearch(Pageable pageable, String searchTerm) {
        Page<Doc> searchResult = docRepository.quickSearch(pageable, searchTerm);

        return docSearchMapper.docListToDocQuickSearchResultList(searchResult.getContent());
    }

    @Override
    public void updateHotness(Long docID) {
        Doc doc = this.getEntityFromIDOnNullThrowException(docID);

        long docPublishDtmEpoch = doc.getPublishDtm().toEpochSecond(ZoneOffset.UTC);
        long t = docPublishDtmEpoch - GenericBusinessConstant.WEB_START_TIME_EPOCH;
        long x = doc.getUpVotes() - doc.getDownVotes();
        long y = 0;
        if (x > 0) {
            y = 1;
        } else if (x < 0) {
            y = -1;
        }
        long absX = Math.abs(x);
        long z = 1;
        if (absX >= 1) {
            z = absX;
        }

        Double hotness = Math.log10(z) + ((double)y * (double)t) / 45000;
        doc.setHotness(hotness);

        docRepository.save(doc);
    }

    @Override
    //https://www.evanmiller.org/how-not-to-sort-by-average-rating.html
    public void updateWilson(Long docID) {
        Doc doc = this.getEntityFromIDOnNullThrowException(docID);
        double upVotes = doc.getUpVotes();
        double totalVotes = doc.getUpVotes() + doc.getDownVotes();
        double z = 1.96;
        double phat = upVotes / totalVotes;
        double wilson = ( phat + z*z/(2*totalVotes) - z*Math.sqrt((phat * (1-phat) + z*z/(4*totalVotes))/totalVotes))/(1+z*z/totalVotes);
        doc.setWilson(wilson);
        docRepository.save(doc);
    }

    @Override
    public void updateUpVotes(Long docID) {
        Doc doc = this.getEntityFromIDOnNullThrowException(docID);
        long docLike = userDocReactionRepository.countByDocReactionTypeAndUserDocReactionIdDocId(DocReactionType.LIKE, docID);
        doc.setUpVotes(docLike);
        docRepository.save(doc);
    }

    @Override
    public void updateDownVotes(Long docID) {
        Doc doc = this.getEntityFromIDOnNullThrowException(docID);
        long docDislike = userDocReactionRepository.countByDocReactionTypeAndUserDocReactionIdDocId(DocReactionType.DISLIKE, docID);
        doc.setDownVotes(docDislike);
        docRepository.save(doc);
    }

    @Override
    public void updateViews(Long docID) {
        Doc doc = this.getEntityFromIDOnNullThrowException(docID);
        long docViews = docViewRepository.countByDocId(docID);
        doc.setViews(docViews);
        docRepository.save(doc);
    }

    @Override
    public void updateDownloads(Long docID) {
        Doc doc = this.getEntityFromIDOnNullThrowException(docID);
        long docDownloads = docDownloadRepository.countByDocFileUploadDocId(docID);
        doc.setDownloads(docDownloads);
        docRepository.save(doc);
    }

    private Doc getEntityFromIDOnNullThrowException (Long docID) {
        Optional<Doc> docOpt = docRepository.findById(docID);
        Validate.isTrue(docOpt.isPresent());
        return docOpt.get();
    }
}
