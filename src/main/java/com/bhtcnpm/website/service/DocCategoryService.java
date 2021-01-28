package com.bhtcnpm.website.service;

import com.bhtcnpm.website.model.dto.DocCategory.DocCategoryDTO;

import java.util.List;

public interface DocCategoryService {

    List<DocCategoryDTO> getDocCategories ();

    DocCategoryDTO createDocCategory(DocCategoryDTO docCategoryDTO);

    DocCategoryDTO updateDocCategory (Long docCategoryId, DocCategoryDTO docCategoryDTO);

    Boolean deleteDocCategory (Long docCategoryId);
}
