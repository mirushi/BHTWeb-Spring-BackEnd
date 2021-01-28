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
import java.util.Optional;

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

    @Override
    public PostCategoryDTO postPostCategory(PostCategoryDTO postCategoryDTO) {

        PostCategory postCategory = postCategoryMapper.postCategoryDTOToPostCategory(postCategoryDTO, null);

        postCategory = postCategoryRepository.save(postCategory);

        return postCategoryMapper.postCategoryToPostCategoryDTO(postCategory);
    }

    @Override
    public PostCategoryDTO putPostCategory(PostCategoryDTO postCategoryDTO, Long postCategoryID) {

        Optional<PostCategory> optionalPostCategory = postCategoryRepository.findById(postCategoryID);

        if (!optionalPostCategory.isPresent()) {
            return null;
        }

        PostCategory postCategory = postCategoryMapper.postCategoryDTOToPostCategory(postCategoryDTO, optionalPostCategory.get());

        postCategory = postCategoryRepository.save(postCategory);

        return postCategoryMapper.postCategoryToPostCategoryDTO(postCategory);
    }
}
