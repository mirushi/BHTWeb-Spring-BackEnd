package com.bhtcnpm.website.service.impl;

import com.bhtcnpm.website.model.dto.DocCategory.DocCategoryDTO;
import com.bhtcnpm.website.model.dto.DocCategory.DocCategoryMapper;
import com.bhtcnpm.website.model.entity.DocEntities.DocCategory;
import com.bhtcnpm.website.repository.DocCategoryRepository;
import com.bhtcnpm.website.service.DocCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DocCategoryServiceImpl implements DocCategoryService {

    private final DocCategoryRepository docCategoryRepository;

    private final DocCategoryMapper docCategoryMapper;

    @Override
    public List<DocCategoryDTO> getDocCategories() {
        List<DocCategory> queryResult = docCategoryRepository.findAll();

        return docCategoryMapper.docCategoryListToDocCategoryDTOList(queryResult);
    }
}
