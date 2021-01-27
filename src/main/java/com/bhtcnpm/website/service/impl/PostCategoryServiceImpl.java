package com.bhtcnpm.website.service.impl;

import com.bhtcnpm.website.model.dto.PostCategory.PostCategoryDTO;
import com.bhtcnpm.website.model.dto.PostCategory.PostCategoryMapper;
import com.bhtcnpm.website.model.entity.PostEntities.PostCategory;
import com.bhtcnpm.website.repository.PostCategoryRepository;
import com.bhtcnpm.website.service.PostCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PostCategoryServiceImpl implements PostCategoryService {

    private final PostCategoryRepository postCategoryRepository;

    private final PostCategoryMapper postCategoryMapper;

    @Override
    public List<PostCategoryDTO> getAllPostCategories() {
        List<PostCategory> queryResult = postCategoryRepository.findAll();

        List<PostCategoryDTO> result = postCategoryMapper.postCategoryListToPostCategoryDTOList(queryResult);

        return result;
    }
}
