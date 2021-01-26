package com.bhtcnpm.website.service.impl;

import com.bhtcnpm.website.model.dto.DocComment.DocCommentListDTO;
import com.bhtcnpm.website.model.dto.DocComment.DocCommentMapper;
import com.bhtcnpm.website.model.dto.DocComment.DocCommentRequestDTO;
import com.bhtcnpm.website.model.entity.DocEntities.DocComment;
import com.bhtcnpm.website.repository.DocCommentRepository;
import com.bhtcnpm.website.service.DocCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DocCommentServiceImpl implements DocCommentService {

    private static final Integer PAGE_SIZE = 10;

    private final DocCommentRepository docCommentRepository;

    private final DocCommentMapper docCommentMapper;

    @Override
    public DocCommentListDTO getDocComment (Integer pageNum, Long docID) {
        Pageable pageable = PageRequest.of(pageNum,PAGE_SIZE);

        Page<DocComment> queryResults = docCommentRepository.findAllByDocId(pageable, docID);

        DocCommentListDTO result = docCommentMapper.docCommentPageToDocCommentListDTO(queryResults);

        return result;
    }

    @Override
    public Boolean postDocComment (DocCommentRequestDTO requestDTO, Long authorID, Long docID) {
        DocComment docComment = docCommentMapper.docCommentDTOToDocComment(requestDTO, authorID, docID, null);

        docCommentRepository.save(docComment);
        return true;
    }
}
