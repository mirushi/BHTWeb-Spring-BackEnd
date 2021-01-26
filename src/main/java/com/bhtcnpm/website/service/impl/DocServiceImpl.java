package com.bhtcnpm.website.service.impl;

import com.bhtcnpm.website.model.dto.Doc.*;
import com.bhtcnpm.website.model.entity.Doc;
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
        int rowChanged = docRepository.postApprove(docID, userID);
        if (rowChanged == 1) {
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public Boolean deleteApproval (Long docID) {
        int rowChanged = docRepository.deleteApprove(docID);
        if (rowChanged == 1) {
            return true;
        }
        return false;
    }
}
