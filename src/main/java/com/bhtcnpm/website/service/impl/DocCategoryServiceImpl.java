package com.bhtcnpm.website.service.impl;

import com.bhtcnpm.website.model.dto.DocCategory.DocCategoryDTO;
import com.bhtcnpm.website.model.dto.DocCategory.DocCategoryMapper;
import com.bhtcnpm.website.model.entity.DocEntities.DocCategory;
import com.bhtcnpm.website.repository.DocCategoryRepository;
import com.bhtcnpm.website.service.DocCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
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

    @Override
    public DocCategoryDTO createDocCategory(DocCategoryDTO docCategoryDTO) {
        DocCategory docCategory = docCategoryMapper.docCategoryDTOToDocCategory(docCategoryDTO, new DocCategory());
        docCategory = docCategoryRepository.save(docCategory);

        return docCategoryMapper.docCategoryToDocCategoryDTO(docCategory);
    }

    @Override
    public DocCategoryDTO updateDocCategory(Long docCategoryId, DocCategoryDTO docCategoryDTO) {

        DocCategory docCategory = docCategoryRepository.getOne(docCategoryId);

        docCategory = docCategoryMapper.docCategoryDTOToDocCategory(docCategoryDTO, docCategory);

        docCategory = docCategoryRepository.save(docCategory);

        return docCategoryMapper.docCategoryToDocCategoryDTO(docCategory);
    }

    @Override
    public Boolean deleteDocCategory(Long docCategoryId) {
        DocCategory category = docCategoryRepository.getOne(docCategoryId);
        docCategoryRepository.delete(category);

        return true;
    }
}
