package com.bhtcnpm.website.service.impl;

import com.bhtcnpm.website.model.dto.Doc.*;
import com.bhtcnpm.website.model.entity.DocEntities.Doc;
import com.bhtcnpm.website.model.entity.enumeration.DocState.DocStateType;
import com.bhtcnpm.website.repository.DocRepository;
import com.bhtcnpm.website.service.DocService;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
@RequiredArgsConstructor
public class DocServiceImpl implements DocService {

    private static final int PAGE_SIZE = 10;

    private static final int PAGE_SIZE_RELATED_DOC = 3;

    private static final int PAGE_SIZE_TRENDING_DOC = 16;

    private final DocDetailsMapper docDetailsMapper;

    private final DocRequestMapper docRequestMapper;

    private final DocRepository docRepository;

    public DocDetailsListDTO getAllDoc (Predicate predicate, @Min(0)Integer paginator) {

        //Create a pagable.
        Pageable pageable = PageRequest.of(paginator, PAGE_SIZE, Sort.by("publishDtm").descending());

        Page<Doc> queryResult = docRepository.findAll(predicate, pageable);

        List<DocDetailsDTO> docDetailsDTOS = StreamSupport
                .stream(queryResult.spliterator(), false)
                .map(docDetailsMapper::docToDocDetailsDTO)
                .collect(Collectors.toList());

        return new DocDetailsListDTO(docDetailsDTOS, queryResult.getTotalPages());
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
}
