package com.bhtcnpm.website.service.impl;

import com.bhtcnpm.website.model.dto.Announcement.AnnouncementMapper;
import com.bhtcnpm.website.model.dto.Doc.DocDetailsDTO;
import com.bhtcnpm.website.model.dto.Doc.DocDetailsListDTO;
import com.bhtcnpm.website.model.dto.Doc.DocDetailsMapper;
import com.bhtcnpm.website.model.entity.Doc;
import com.bhtcnpm.website.repository.DocRepository;
import com.bhtcnpm.website.service.DocService;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Visitor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.transaction.Transactional;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
@RequiredArgsConstructor
public class DocServiceImpl implements DocService {

    private static final int PAGE_SIZE = 10;

    private final DocDetailsMapper docDetailsMapper;

    private final DocRepository docRepository;

    public DocDetailsListDTO getAllDoc (Predicate predicate, @Min(0)Integer paginator) {

        //Create a pagable.
        Pageable pageable = PageRequest.of(paginator, PAGE_SIZE, Sort.by("publishedDtm").descending());

        Page<Doc> queryResult = docRepository.findAll(predicate, pageable);

        List<DocDetailsDTO> docDetailsDTOS = StreamSupport
                .stream(queryResult.spliterator(), false)
                .map(docDetailsMapper::docToDocDetailsDTO)
                .collect(Collectors.toList());

        DocDetailsListDTO result = new DocDetailsListDTO(docDetailsDTOS, queryResult.getTotalPages());

        return result;
    }
}
