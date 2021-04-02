package com.bhtcnpm.website.service.impl;

import com.bhtcnpm.website.constant.business.Doc.AllowedUploadExtension;
import com.bhtcnpm.website.constant.business.Doc.DocFileUploadConstant;
import com.bhtcnpm.website.model.dto.Doc.*;
import com.bhtcnpm.website.model.entity.DocEntities.Doc;
import com.bhtcnpm.website.model.entity.DocEntities.DocFileUpload;
import com.bhtcnpm.website.model.entity.enumeration.DocReaction.DocReactionType;
import com.bhtcnpm.website.repository.DocFileUploadRepository;
import com.bhtcnpm.website.model.entity.UserWebsite;
import com.bhtcnpm.website.model.entity.enumeration.DocState.DocStateType;
import com.bhtcnpm.website.model.exception.FileExtensionNotAllowedException;
import com.bhtcnpm.website.repository.DocCommentRepository;
import com.bhtcnpm.website.repository.DocRepository;
import com.bhtcnpm.website.repository.UserDocReactionRepository;
import com.bhtcnpm.website.repository.UserWebsiteRepository;
import com.bhtcnpm.website.service.DocService;
import com.bhtcnpm.website.service.GoogleDriveService;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import javax.validation.constraints.Min;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
@RequiredArgsConstructor
public class DocServiceImpl implements DocService {

    private static final int PAGE_SIZE = 10;

    private static final int PAGE_SIZE_RELATED_DOC = 3;

    private static final int PAGE_SIZE_TRENDING_DOC = 16;

    private static final int FILE_NAME_RANDOM_LENGTH = 10;

    private static final String DRIVE_UPLOAD_DEFAULT_FOLDER_ID = "1mg_iZfewkU93WhcFfKYf38irvW1Gr-wn";

    private final DocDetailsMapper docDetailsMapper;

    private final DocRequestMapper docRequestMapper;

    private final DocDownloadInfoMapper docDownloadInfoMapper;

    private final DocRepository docRepository;

    private final DocFileUploadRepository docFileUploadRepository;

    private final DocCommentRepository docCommentRepository;

    private final UserDocReactionRepository userDocReactionRepository;

    private final UserWebsiteRepository userWebsiteRepository;

    public DocDetailsListDTO getAllDoc (Predicate predicate, @Min(0)Integer paginator) {

        //Create a pagable.
        Pageable pageable = PageRequest.of(paginator, PAGE_SIZE, Sort.by("publishDtm").descending());

        Page<Doc> queryResult = docRepository.findAll(predicate, pageable);

        List<DocDetailsDTO> docDetailsDTOS = StreamSupport
                .stream(queryResult.spliterator(), false)
                .map(docDetailsMapper::docToDocDetailsDTO)
                .collect(Collectors.toList());

        return new DocDetailsListDTO(docDetailsDTOS, queryResult.getTotalPages(), queryResult.getTotalElements());
    }

    @Override
    public DocDetailsDTO putDoc(Long docID, Long lastEditedUserID, DocRequestDTO docRequestDTO) {
        Doc oldDoc = null;

        if (docID != null) {
            Optional<Doc> oldDocQuery = docRepository.findById(docID);
            if (oldDocQuery.isPresent()) {
                oldDoc = oldDocQuery.get();
            }
        }

        Doc doc = docRequestMapper.updateDocFromDocRequestDTO(lastEditedUserID, docRequestDTO, oldDoc);

        doc = docRepository.save(doc);

        return docDetailsMapper.docToDocDetailsDTO(doc);
    }

    @Override
    @Transactional
    public Boolean postApproval(Long docID, Long userID) {
        int rowChanged = docRepository.setDocState(docID, DocStateType.APPROVED);
        if (rowChanged == 1) {
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public Boolean deleteApproval (Long docID) {
        int rowChanged = docRepository.setDocState(docID, DocStateType.PENDING_APPROVAL);
        if (rowChanged == 1) {
            return true;
        }
        return false;
    }

    @Override
    public Boolean increaseDownloadCount(Long docID, Long userID) {
        //TODO: Please check condition before increase download count;
        int rowChanged = docRepository.incrementDownloadCount(docID);
        if (rowChanged == 1) {
            return true;
        }
        return false;
    }

    @Override
    public Boolean postReject(Long docID, Long userID) {
        //TODO: Please check condition before allowing doc approval;

        int rowChanged = docRepository.setDocState(docID, DocStateType.REJECTED);
        if (rowChanged == 1) {
            return true;
        }

        return false;
    }

    @Override
    public Boolean undoReject(Long docID, Long userID) {
        //TODO: Please check condition before allowing doc approval;
        int rowChanged = docRepository.setDocState(docID, DocStateType.PENDING_APPROVAL);

        if (rowChanged == 1) {
            return true;
        }

        return false;
    }

    @Override
    public List<DocDetailsDTO> getRelatedDocs(Long docID) {
        //TODO: Please implement a real getRelatedDocs function.
        Pageable pageable = PageRequest.of(0, PAGE_SIZE_RELATED_DOC);

        List<Doc> docs = docRepository.getDocByIdNot(pageable, docID);

        return docDetailsMapper.docListToDocDetailsDTOList(docs);
    }

    @Override
    public DocDetailsDTO createDocument(DocRequestDTO docRequestDTO) {
//        docRequestMapper.updateDocFromDocRequestDTO()
        return null;
    }

    @Override
    public List<DocSummaryDTO> getTrending() {
        Pageable pageable = PageRequest.of(0, PAGE_SIZE_TRENDING_DOC);

        return docRepository.getTrendingDoc(pageable);
    }

    @Override
    public List<DocStatisticDTO> getDocStatistics(List<Long> docIDs, Long userID) {

        List<DocReactionStatisticDTO> docReactionStatisticDTOs = userDocReactionRepository.getDocReactionStatisticsDTO(docIDs);

        List<DocUserOwnReactionStatisticDTO> docUserOwnReactionStatisticDTOs = userDocReactionRepository.getDocUserOwnReactionStatisticDTO(docIDs, userID);

        List<DocCommentStatisticDTO> docCommentStatisticDTOs = docCommentRepository.getDocCommentStatistic(docIDs);

        //Tạo ra một HashMap để search nhanh ra các Statistic ứng với từng docID.
        Map<Long, DocReactionStatisticDTO> docReactionStatisticDTOMap = new HashMap<>();
        Map<Long, DocUserOwnReactionStatisticDTO> docUserOwnReactionStatisticDTOMap = new HashMap<>();
        Map<Long, DocCommentStatisticDTO> docCommentStatisticDTOMap = new HashMap<>();

        for (DocReactionStatisticDTO dto : docReactionStatisticDTOs) {
            docReactionStatisticDTOMap.put(dto.getDocID(), dto);
        }
        for (DocUserOwnReactionStatisticDTO dto : docUserOwnReactionStatisticDTOs) {
            docUserOwnReactionStatisticDTOMap.put(dto.getDocID(), dto);
        }
        for (DocCommentStatisticDTO dto : docCommentStatisticDTOs) {
            docCommentStatisticDTOMap.put(dto.getDocID(), dto);
        }

        int totalIDs = docIDs.size();

        List<DocStatisticDTO> resultList = new ArrayList<>(totalIDs);

        for (int i = 0;i < totalIDs; ++i) {
            Long docID = docIDs.get(i);

            DocReactionStatisticDTO docReactionStatisticDTO = docReactionStatisticDTOMap.get(docID);
            if (docReactionStatisticDTO == null) {
                docReactionStatisticDTO = DocReactionStatisticDTO.builder()
                        .docID(docID)
                        .dislikeCount(0L)
                        .likeCount(0L)
                        .build();
            }
            DocUserOwnReactionStatisticDTO docUserOwnReactionStatisticDTO = docUserOwnReactionStatisticDTOMap.get(docID);
            if (docUserOwnReactionStatisticDTO == null) {
                docUserOwnReactionStatisticDTO = DocUserOwnReactionStatisticDTO.builder()
                        .docID(docID)
                        .docReactionType(DocReactionType.NONE)
                        .build();
            }
            DocCommentStatisticDTO docCommentStatisticDTO = docCommentStatisticDTOMap.get(docID);
            if (docCommentStatisticDTO == null) {
                docCommentStatisticDTO = DocCommentStatisticDTO.builder()
                        .docID(docID)
                        .commentCount(0L)
                        .build();
            }

            resultList.add(new DocStatisticDTO(docID,
                    docReactionStatisticDTO.getLikeCount(), docReactionStatisticDTO.getDislikeCount(),
                    docUserOwnReactionStatisticDTO.getDocReactionType(), docCommentStatisticDTO.getCommentCount()));
        }

        return resultList;
    }

    @Override
    public DocDetailsDTO createDoc(DocRequestDTO docRequestDTO, Long userID) {

        Doc doc = docRequestMapper.updateDocFromDocRequestDTO(userID, docRequestDTO, null);

        doc = docRepository.save(doc);

        return docDetailsMapper.docToDocDetailsDTO(doc);
    }

    @Override
    public DocSummaryListDTO getPostBySearchTerm(Predicate predicate, Pageable pageable, String searchTerm) {
        //Reset PAGE_SIZE to predefined value.
        pageable = PageRequest.of(pageable.getPageNumber(), PAGE_SIZE, pageable.getSort());

        DocSummaryListDTO queryResult = docRepository.searchBySearchTerm(predicate, pageable, searchTerm);

        return queryResult;
    }

    @Override
    public DocUploadDTO uploadFileToGDrive(MultipartFile multipartFile, Long userID) throws IOException, FileExtensionNotAllowedException {
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
            .uploader(author)
            .build();

        fileUpload = docFileUploadRepository.save(fileUpload);

        return DocUploadDTO.builder()
                .fileName(fileUpload.getFileName())
                .code(fileUpload.getCode())
                .fileSize(multipartFile.getSize())
                .build();
    }

    @Override
    public DocDownloadInfoDTO getDocDownloadInfo(String fileCode) {
        UUID uuid = UUID.fromString(fileCode);

        DocFileUpload file = docFileUploadRepository.findByCode(UUID.fromString(fileCode));

        return docDownloadInfoMapper.docFileUploadToDocDownloadInfoDTO(file);
    }
}
